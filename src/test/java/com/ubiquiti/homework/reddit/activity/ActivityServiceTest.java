package com.ubiquiti.homework.reddit.activity;

import com.ubiquiti.homework.reddit.activity.api.SearchParams;
import com.ubiquiti.homework.reddit.activity.api.TimeRange;
import com.ubiquiti.homework.reddit.activity.handler.Event;
import com.ubiquiti.homework.reddit.activity.handler.ActivityType;
import com.ubiquiti.homework.time.TimeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.ubiquiti.homework.reddit.activity.RedditActivity.FIELD_NAME;
import static com.ubiquiti.homework.reddit.activity.RedditActivity.FIELD_TYPE;
import static com.ubiquiti.homework.reddit.activity.RedditActivity.FIELD_USER;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.COMMENT;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.SUBMISSION;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    private static final LocalDateTime DATE_TIME = LocalDateTime.now();
    private static final TimeRange RANGE = TimeRange.FIVE_MINUTES;
    private static final long RANGE_MINUTES = RANGE.getMinutes();

    @Mock private ActivityRepository repository;
    @Mock private TimeService timeService;

    @InjectMocks private ActivityService service;

    @Test
    void save() {
        String subreddit = "Bar";
        String author = "Foo";
        ActivityType type = COMMENT;

        Event event = new Event.Builder()
                .subreddit(subreddit)
                .author(author)
                .build();

        RedditActivity expected = RedditActivity.builder()
                .created(DATE_TIME)
                .subreddit(subreddit)
                .user(author)
                .type(type)
                .build();

        when(timeService.currentDateTime()).thenReturn(DATE_TIME);

        service.save(event, type);

        verify(repository).save(expected);
    }

    @Test
    void activity() {
        ActivitySum expected = ActivitySum.builder()
                .comments(33L)
                .submissions(21L)
                .build();

        when(timeService.minutesBeforeNow(RANGE_MINUTES)).thenReturn(DATE_TIME);
        when(repository.groupByAndCount(DATE_TIME, FIELD_TYPE, page(2)))
                .thenReturn(Arrays.asList(
                        result(COMMENT.name(), 33L),
                        result(SUBMISSION.name(), 21L)));

        ActivitySum actual = service.activity(RANGE);

        assertThat(actual).isEqualTo(expected);

        verify(timeService).minutesBeforeNow(RANGE_MINUTES);
        verify(repository).groupByAndCount(DATE_TIME, FIELD_TYPE, page(2));
    }

    @Test
    void activity_default() {
        ActivitySum expected = ActivitySum.builder()
                .comments(0L)
                .submissions(0L)
                .build();

        when(timeService.minutesBeforeNow(RANGE_MINUTES)).thenReturn(DATE_TIME);
        when(repository.groupByAndCount(DATE_TIME, FIELD_TYPE, page(2)))
                .thenReturn(emptyList());

        ActivitySum actual = service.activity(RANGE);

        assertThat(actual).isEqualTo(expected);

        verify(timeService).minutesBeforeNow(RANGE_MINUTES);
        verify(repository).groupByAndCount(DATE_TIME, FIELD_TYPE, page(2));
    }

    @Test
    void subreddits() {
        SearchParams params = new SearchParams(RANGE, 0, 50);

        assertAndVerify(FIELD_NAME, () -> service.subreddits(params));
    }

    @Test
    void users() {
        SearchParams params = new SearchParams(RANGE, 0, 50);

        assertAndVerify(FIELD_USER, () -> service.users(params));
    }

    private GroupByResult result(String name, Long activity) {
        GroupByResult result = new GroupByResult();
        result.setName(name);
        result.setActivity(activity);
        return result;
    }

    private void assertAndVerify(String fieldName, Supplier<List<GroupByResult>> supplier) {
        Pageable page = page(50);

        when(timeService.minutesBeforeNow(RANGE_MINUTES)).thenReturn(DATE_TIME);
        when(repository.groupByAndCount(DATE_TIME, fieldName, page))
                .thenReturn(Arrays.asList(
                        result("name1", 1L),
                        result("name2", 2L)));

        List<GroupByResult> actual = supplier.get();

        assertThat(actual).containsExactly(
                result("name1", 1L),
                result("name2", 2L));

        verify(timeService).minutesBeforeNow(RANGE_MINUTES);
        verify(repository).groupByAndCount(DATE_TIME, fieldName, page);
    }

    private PageRequest page(int size) {
        return PageRequest.of(0, size);
    }
}
