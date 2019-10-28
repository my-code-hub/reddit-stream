package com.ubiquiti.homework.reddit.activity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class GroupByResult {

    @Id private String name;
    private Long activity;
}
