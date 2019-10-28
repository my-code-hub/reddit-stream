package com.ubiquiti.homework.reddit.activity.handler;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityType {

    COMMENT("rc"), SUBMISSION("rs");

    private String code;

    static Optional<ActivityType> from(String code) {
        return Stream.of(values())
                .filter(event -> event.getCode().equals(code))
                .findAny();
    }
}
