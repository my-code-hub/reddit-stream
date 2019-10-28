package com.ubiquiti.homework.configuration;

import com.launchdarkly.eventsource.EventSource;
import com.ubiquiti.homework.reddit.activity.handler.RedditActivityHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConditionalOnProperty(prefix = "reddit-stream", name = "enabled")
public class EventSourceConfiguration {

    @Value("${reddit-stream.redditStreamUri}") URI redditStreamUri;

    @Bean
    public EventSource redditEventSource(RedditActivityHandler handler) {
        return new EventSource.Builder(handler, redditStreamUri).build();
    }
}
