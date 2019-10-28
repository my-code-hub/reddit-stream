package com.ubiquiti.homework.reddit.activity.api;

import lombok.Getter;

@Getter
public class SearchParams {

    private TimeRange range;
    private Integer page;
    private Integer size;

    public SearchParams(TimeRange range, Integer page, Integer size) {
        this.range = range;
        this.page = ensureMinValue(page, 0);
        this.size = ensureMinValue(size, 1);
    }

    private Integer ensureMinValue(Integer number, Integer minValue) {
        return number < minValue ? minValue : number;
    }
}
