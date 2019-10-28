package com.ubiquiti.homework.reddit.activity.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeRange {

    MINUTE(1),

    FIVE_MINUTES(5),
    HOUR(60),
    DAY(1440),
    ALL_TIME(60 * 24 * 365 * 50); //50 years

    private long minutes;
}
