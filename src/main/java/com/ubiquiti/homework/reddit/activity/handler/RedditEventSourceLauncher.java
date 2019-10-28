package com.ubiquiti.homework.reddit.activity.handler;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.ReadyState;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(prefix = "reddit-stream", name = "enabled")
class RedditEventSourceLauncher {

    private final EventSource eventSource;

    @EventListener(ApplicationReadyEvent.class)
    public void handle(ApplicationReadyEvent event) {
        if (ReadyState.OPEN != eventSource.getState()) {
            eventSource.start();
        }
    }
}
