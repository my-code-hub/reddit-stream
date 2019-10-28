package com.ubiquiti.homework.reddit.activity.api;

import com.ubiquiti.homework.reddit.activity.ActivityService;
import com.ubiquiti.homework.reddit.activity.ActivitySum;
import com.ubiquiti.homework.reddit.activity.GroupByResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PACKAGE;

@RestController
@RequestMapping("/v1/reddit/activity")
@AllArgsConstructor(access = PACKAGE)
class ActivityController {

    private static final String RANGE = "DAY";
    private static final String PAGE = "0";
    private static final String PAGE_SIZE = "100";

    private final ActivityService service;

    @GetMapping
    public ActivitySum activity(
            @RequestParam(required = false, defaultValue = RANGE) TimeRange range) {
        return service.activity(range);
    }

    @GetMapping("/subreddits")
    public List<GroupByResult> subreddits(
            @RequestParam(required = false, defaultValue = RANGE) TimeRange range,
            @RequestParam(required = false, defaultValue = PAGE) Integer page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE) Integer size) {
        return service.subreddits(new SearchParams(range, page, size));
    }

    @GetMapping("/users")
    public List<GroupByResult> users(
            @RequestParam(required = false, defaultValue = RANGE) TimeRange range,
            @RequestParam(required = false, defaultValue = PAGE) Integer page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE) Integer size) {
        return service.users(new SearchParams(range, page, size));
    }
}
