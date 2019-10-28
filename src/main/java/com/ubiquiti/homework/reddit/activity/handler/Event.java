package com.ubiquiti.homework.reddit.activity.handler;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = Event.Builder.class)
public class Event {

    private String subreddit;
    private String author;

    private Event(Builder builder) {
        subreddit = builder.subreddit;
        author = builder.author;
    }

    @JsonPOJOBuilder(withPrefix = "")
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static final class Builder {
        private String subreddit;
        private String author;

        public Builder subreddit(String val) {
            subreddit = val;
            return this;
        }

        public Builder author(String val) {
            author = val;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
