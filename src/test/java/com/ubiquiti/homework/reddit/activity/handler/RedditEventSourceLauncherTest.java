package com.ubiquiti.homework.reddit.activity.handler;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.ReadyState;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedditEventSourceLauncherTest {

    @Mock private EventSource eventSource;

    @InjectMocks private RedditEventSourceLauncher launcher;

    @Mock private ApplicationReadyEvent event;

    @Test
    void onApplicationEvent_stateOpen() {
        when(eventSource.getState()).thenReturn(ReadyState.OPEN);

        launcher.handle(event);

        verify(eventSource).getState();
        verifyNoMoreInteractions(eventSource);
        verifyNoInteractions(event);
    }

    @Test
    void onApplicationEvent_stateNotOpen() {
        when(eventSource.getState()).thenReturn(ReadyState.CLOSED);

        launcher.handle(event);

        verify(eventSource).getState();
        verify(eventSource).start();
        verifyNoMoreInteractions(eventSource);
        verifyNoInteractions(event);
    }
}
