package com.ubiquiti.homework.reddit.activity.api;

import com.ubiquiti.homework.reddit.activity.ActivityRepository;
import com.ubiquiti.homework.reddit.activity.RedditActivity;
import com.ubiquiti.homework.time.TimeService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ubiquiti.homework.error.ErrorHandler.BAD_REQUEST_MESSAGE;
import static com.ubiquiti.homework.reddit.activity.api.TimeRange.HOUR;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.COMMENT;
import static com.ubiquiti.homework.reddit.activity.handler.ActivityType.SUBMISSION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ActivityControllerIT {

    @Autowired private ActivityRepository repository;
    @Autowired private TimeService timeService;

    @Autowired private MockMvc mvc;

    private LocalDateTime CREATED;

    @Before
    public void beforeEach() {
        repository.deleteAll();
        CREATED = timeService.currentDateTime();
    }

    @Test
    public void activity_empty() throws Exception {
        JSONObject expected = new JSONObject()
                .put("submissions", 0L)
                .put("comments", 0L);

        mvc.perform(get("/v1/reddit/activity"))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void activity() throws Exception {
        insert(comment(),
                comment().subreddit("subreddit1").user("user2"),
                submission(),
                submission().user("user3"),
                submission().subreddit("subreddit3"));

        JSONObject expected = new JSONObject()
                .put("submissions", 3L)
                .put("comments", 2L);

        mvc.perform(get("/v1/reddit/activity"))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void activity_range() throws Exception {
        LocalDateTime twoHoursBefore = CREATED.minusHours(2);

        insert(comment().created(twoHoursBefore),
                comment().subreddit("subreddit1").user("user2"),
                submission().created(twoHoursBefore),
                submission().user("user3"));

        JSONObject expected = new JSONObject()
                .put("submissions", 1L)
                .put("comments", 1L);

        mvc.perform(get("/v1/reddit/activity?range={range}", HOUR.name()))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void activity_invalidRange() throws Exception {
        JSONObject expected = new JSONObject()
                .put("message", BAD_REQUEST_MESSAGE);

        mvc.perform(get("/v1/reddit/activity?range={range}", "FOO"))
                .andExpect(status().is(BAD_REQUEST.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void subreddits() throws Exception {
        LocalDateTime twoDaysBefore = CREATED.minusDays(2);

        insert(comment(), comment(), comment(), submission(), submission(),
                submission().subreddit("subreddit3"), comment().subreddit("subreddit3"), comment().subreddit("subreddit3"),
                submission().subreddit("subreddit2"), submission().subreddit("subreddit2"),
                comment().subreddit("subreddit4"),
                comment().subreddit("subreddit5").created(twoDaysBefore));

        JSONArray expected = new JSONArray()
                .put(activity("subreddit1", 5L))
                .put(activity("subreddit3", 3L))
                .put(activity("subreddit2", 2L))
                .put(activity("subreddit4", 1L));

        mvc.perform(get("/v1/reddit/activity/subreddits"))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void subreddits_paging() throws Exception {
        insert(comment(), submission(),
                submission().subreddit("subreddit3"));

        JSONArray expected = new JSONArray()
                .put(activity("subreddit1", 2L));

        mvc.perform(get("/v1/reddit/activity/subreddits?page={page}&size={size}", -1, -1))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void users() throws Exception {
        LocalDateTime twoDaysBefore = CREATED.minusDays(2);

        insert(comment(), comment(), comment(), submission(), submission(),
                submission().user("user3"), comment().user("user3"), comment().user("user3"),
                submission().user("user2"), submission().user("user2"),
                comment().user("user4"),
                comment().user("user5").created(twoDaysBefore));

        JSONArray expected = new JSONArray()
                .put(activity("user1", 5L))
                .put(activity("user3", 3L))
                .put(activity("user2", 2L))
                .put(activity("user4", 1L));

        mvc.perform(get("/v1/reddit/activity/users"))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    @Test
    public void users_paging() throws Exception {
        insert(comment(), submission(),
                submission().user("user2"));

        JSONArray expected = new JSONArray()
                .put(activity("user1", 2L));

        mvc.perform(get("/v1/reddit/activity/users?page={page}&size={size}", -1, -1))
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json(expected.toString(), true));
    }

    private void insert(RedditActivity.RedditActivityBuilder... documents) {
        List<RedditActivity> data = Arrays.stream(documents)
                .map(RedditActivity.RedditActivityBuilder::build)
                .collect(Collectors.toList());
        repository.saveAll(data);
    }

    private RedditActivity.RedditActivityBuilder comment() {
        return RedditActivity.builder()
                .subreddit("subreddit1")
                .user("user1")
                .created(CREATED)
                .type(COMMENT);
    }

    private RedditActivity.RedditActivityBuilder submission() {
        return comment().type(SUBMISSION);
    }

    private JSONObject activity(String name, long activity) throws Exception {
        return new JSONObject()
                .put("name", name)
                .put("activity", activity);
    }
}
