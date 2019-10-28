package com.ubiquiti.homework.reddit.activity.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedditEventMapperTest {

    @Mock private ObjectMapper objectMapper;

    @InjectMocks private RedditEventMapper mapper;

    @Test
    void toRedditEvent_success() throws Exception {
        String json = "{}";
        Event event = new Event.Builder().subreddit("foo").build();

        when(objectMapper.readValue(json, Event.class)).thenReturn(event);

        Optional<Event> actual = mapper.toRedditEvent(json);

        assertThat(actual).isPresent().hasValue(event);
    }

    @Test
    void toRedditEvent_exception() throws Exception {
        String json = "{}";
        String errorMessage = "error";

        JsonMappingException error = mock(JsonMappingException.class);

        when(objectMapper.readValue(json, Event.class)).thenThrow(error);
        when(error.getMessage()).thenReturn(errorMessage);

        Optional<Event> actual = mapper.toRedditEvent(json);

        assertThat(actual).isEmpty();
    }
}
