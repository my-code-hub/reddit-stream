package com.ubiquiti.homework.reddit.activity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
class RedditEventMapper {

    private final ObjectMapper objectMapper;

    Optional<Event> toRedditEvent(String json) {
        try {
            return ofNullable(objectMapper.readValue(json, Event.class));
        } catch (IOException e) {
            log.error("error de-serializing event {}", json, e);
        }
        return empty();
    }
}
