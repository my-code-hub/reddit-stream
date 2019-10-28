package com.ubiquiti.homework.reddit.activity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends MongoRepository<RedditActivity, String> {

    @Aggregation({
            "{ $match : { created : { $gte : ?0 }}}",
            "{ $group : { _id : \"$?1\", activity : { $sum : 1 }}}",
            "{ $sort  : { activity : -1}}"
    })
    List<GroupByResult> groupByAndCount(LocalDateTime from, String groupByField, Pageable pageable);
}
