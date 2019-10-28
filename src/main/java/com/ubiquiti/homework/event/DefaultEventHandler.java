package com.ubiquiti.homework.event;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultEventHandler implements EventHandler {

    @Override
    public void onOpen() throws Exception {
        log.info("connection open");
    }

    @Override
    public void onClosed() throws Exception {
        log.info("connection closed");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {

    }

    @Override
    public void onComment(String comment) throws Exception {

    }

    @Override
    public void onError(Throwable t) {
        log.error("error handling event", t);
    }
}
