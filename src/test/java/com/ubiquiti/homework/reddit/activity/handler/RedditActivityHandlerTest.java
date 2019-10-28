package com.ubiquiti.homework.reddit.activity.handler;

import com.launchdarkly.eventsource.MessageEvent;
import com.ubiquiti.homework.reddit.activity.ActivityService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedditActivityHandlerTest {

    @Mock private RedditEventMapper mapper;
    @Mock private ActivityService service;

    @InjectMocks private RedditActivityHandler handler;

    @Test
    void onMessage_eventIsNull() {
        handler.onMessage(null, null);

        verifyNoInteractions(mapper, service);
    }

    @Test
    void onMessage_unsupportedEvent() {
        handler.onMessage("unsupported", null);

        verifyNoInteractions(mapper, service);
    }

    @Test
    void onMessage_eventDataIsNull() {
        handler.onMessage("rc", messageEvent(null));

        verifyNoInteractions(mapper, service);
    }

    @Test
    void onMessage_mapperReturnsEmptyResult() {
        handler.onMessage("rc", messageEvent("{}"));

        verify(mapper).toRedditEvent("{}");
        verifyNoInteractions(service);
    }

    @Test
    void onMessage_success() {
        String eventData = "{}";
        Event redditEvent = redditEvent(eventData);

        when(mapper.toRedditEvent(eventData)).thenReturn(Optional.of(redditEvent));

        handler.onMessage("rc", messageEvent(eventData));

        verify(mapper).toRedditEvent(eventData);
        verify(service).save(redditEvent, ActivityType.COMMENT);
    }

    private MessageEvent messageEvent(String data) {
        return new MessageEvent(data, null, null);
    }

    private Event redditEvent(String subreddit) {
        return new Event.Builder()
                .subreddit(subreddit)
                .build();
    }
}
