package com.ubiquiti.homework.reddit.activity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ActivitySum {

    private Long submissions;
    private Long comments;
}
