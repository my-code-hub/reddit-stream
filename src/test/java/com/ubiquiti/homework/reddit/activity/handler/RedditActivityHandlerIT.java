package com.ubiquiti.homework.reddit.activity.handler;

import com.ubiquiti.homework.reddit.activity.ActivityRepository;
import com.ubiquiti.homework.reddit.activity.RedditActivity;
import com.ubiquiti.homework.time.TimeService;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.COMMENT;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.SUBMISSION;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "reddit-stream.enabled=true")
public class RedditActivityHandlerIT {

    private static final LocalDateTime CREATED = LocalDateTime.now();

    @Autowired private TestRedditStreamService redditStream;
    @Autowired private ActivityRepository repository;

    @MockBean private TimeService timeService;

    @Before
    public void beforeEach() throws Exception {
        repository.deleteAll();
        when(timeService.currentDateTime()).thenReturn(CREATED);

        sleep(500);
    }

    @Test
    public void onMessage_handleSubredditComment() throws Exception {
        redditStream.push("rc", eventData());

        verifyResultForActivityType(COMMENT);
    }

    @Test
    public void onMessage_handleSubredditSubmission() throws Exception {
        redditStream.push("rs", eventData());

        verifyResultForActivityType(SUBMISSION);
    }

    private void sleep(long timeout) throws InterruptedException {
        MILLISECONDS.sleep(timeout);
    }

    private String eventData() throws JSONException {
        return new JSONObject()
                .put("subreddit", "Foo")
                .put("author", "JohnDoe")
                .put("bar", "this field/value should be ignored").toString();
    }

    private void verifyResultForActivityType(ActivityType type) throws InterruptedException {
        sleep(300);

        List<RedditActivity> result = repository.findAll();
        assertThat(result).hasSize(1);

        RedditActivity actual = result.get(0);
        assertThat(actual.getSubreddit()).isEqualTo("Foo");
        assertThat(actual.getUser()).isEqualTo("JohnDoe");
        assertThat(actual.getType()).isEqualTo(type);
        assertThat(actual.getCreated().withNano(0)).isEqualTo(CREATED.withNano(0));
    }
}
