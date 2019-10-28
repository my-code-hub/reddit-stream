package com.ubiquiti.homework.reddit.activity;

import com.ubiquiti.homework.reddit.activity.api.SearchParams;
import com.ubiquiti.homework.reddit.activity.api.TimeRange;
import com.ubiquiti.homework.reddit.activity.handler.ActivityType;
import com.ubiquiti.homework.reddit.activity.handler.Event;
import com.ubiquiti.homework.time.TimeService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import static com.ubiquiti.homework.reddit.activity.RedditActivity.FIELD_NAME;
import static com.ubiquiti.homework.reddit.activity.RedditActivity.FIELD_TYPE;
import static com.ubiquiti.homework.reddit.activity.RedditActivity.FIELD_USER;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.COMMENT;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.SUBMISSION;
import static lombok.AccessLevel.PACKAGE;

@Service
@AllArgsConstructor(access = PACKAGE)
public class ActivityService {

    private final ActivityRepository repository;
    private final TimeService timeService;

    public void save(Event subreddit, ActivityType type) {
        repository.save(new RedditActivity(subreddit, type, timeService.currentDateTime()));
    }

    public ActivitySum activity(TimeRange range) {
        Map<String, Long> sumByActivityType = sumByActivityType(range);
        return ActivitySum.builder()
                .comments(sumByActivityType.getOrDefault(COMMENT.name(), 0L))
                .submissions(sumByActivityType.getOrDefault(SUBMISSION.name(), 0L))
                .build();
    }

    public List<GroupByResult> subreddits(SearchParams params) {
        return groupByAndCount(params, FIELD_NAME);
    }

    public List<GroupByResult> users(SearchParams params) {
        return groupByAndCount(params, FIELD_USER);
    }

    private Map<String, Long> sumByActivityType(TimeRange range) {
        return repository.groupByAndCount(from(range), FIELD_TYPE, page(0, 2))
                .stream().collect(Collectors.toMap(
                        GroupByResult::getName,
                        GroupByResult::getActivity));
    }

    private List<GroupByResult> groupByAndCount(SearchParams params, String groupBy) {
        Pageable page = page(params.getPage(), params.getSize());
        return repository.groupByAndCount(from(params.getRange()), groupBy, page);
    }

    private LocalDateTime from(TimeRange range) {
        return timeService.minutesBeforeNow(range.getMinutes());
    }

    private PageRequest page(int page, int size) {
        return PageRequest.of(page, size);
    }
}
