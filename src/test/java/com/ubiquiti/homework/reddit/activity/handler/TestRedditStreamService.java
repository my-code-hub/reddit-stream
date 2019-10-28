package com.ubiquiti.homework.reddit.activity.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
class TestRedditStreamService {

    private static final Logger log = LoggerFactory.getLogger(TestRedditStreamService.class);

    private final List<SseEmitter> emitters = new ArrayList<>();

    SseEmitter init() {
        SseEmitter emitter = new SseEmitter();
        initHandlers(emitter);
        emitters.add(emitter);
        return emitter;
    }

    void push(String eventName, String eventData) {
        SseEmitter.SseEventBuilder event = event(eventName, eventData);
        emitters.parallelStream()
                .forEach(emitter -> send(emitter, event));
    }

    private void initHandlers(SseEmitter emitter) {
        emitter.onCompletion(() -> remove(emitter));
        emitter.onError(throwable -> {
            log.error("emitter {} error", emitter, throwable);
            remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.error("emitter {} timeout", emitter);
            remove(emitter);
        });
    }

    private void remove(SseEmitter emitter) {
       emitters.remove(emitter);
    }

    private SseEmitter.SseEventBuilder event(String eventName, String eventData) {
        return SseEmitter.event()
                .name(eventName)
                .data(eventData);
    }

    private void send(SseEmitter emitter, SseEmitter.SseEventBuilder event) {
        try {
            emitter.send(event);
        } catch (IOException e) {
            log.error("error sending event {}", event, e);
            emitter.complete();
        }
    }
}
