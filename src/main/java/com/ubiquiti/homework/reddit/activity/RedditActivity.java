package com.ubiquiti.homework.reddit.activity;

import com.ubiquiti.homework.reddit.activity.handler.Event;
import com.ubiquiti.homework.reddit.activity.handler.ActivityType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RedditActivity {

    public static final String FIELD_NAME = "subreddit";
    public static final String FIELD_USER = "user";
    public static final String FIELD_TYPE = "type";

    @Id private String id;
    private String subreddit;
    private String user;
    @Indexed private LocalDateTime created;
    private ActivityType type;

    RedditActivity(Event event, ActivityType type, LocalDateTime created) {
        this.subreddit = event.getSubreddit();
        this.user = event.getAuthor();
        this.type = type;
        this.created = created;
    }
}
