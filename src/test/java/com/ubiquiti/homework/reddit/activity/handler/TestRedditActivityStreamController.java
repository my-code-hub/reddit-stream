package com.ubiquiti.homework.reddit.activity.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
class TestRedditActivityStreamController {

    @Autowired private TestRedditStreamService service;

    @GetMapping(path = "/reddit-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter redditStream() {
        return service.init();
    }
}
