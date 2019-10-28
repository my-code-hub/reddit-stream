package com.ubiquiti.homework.reddit.activity.handler;

import com.launchdarkly.eventsource.MessageEvent;
import com.ubiquiti.homework.event.DefaultEventHandler;
import com.ubiquiti.homework.reddit.activity.ActivityService;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import static java.util.Optional.ofNullable;

@Component
@AllArgsConstructor
public class RedditActivityHandler extends DefaultEventHandler {

    private final RedditEventMapper mapper;
    private final ActivityService service;

    @Override
    public void onMessage(String eventName, MessageEvent event) {
        ActivityType.from(eventName)
                .ifPresent(eventType -> handle(eventType, event.getData()));
    }

    private void handle(ActivityType type, String event) {
        ofNullable(event)
                .flatMap(mapper::toRedditEvent)
                .ifPresent(subreddit -> service.save(subreddit, type));
    }
}
