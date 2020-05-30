package com.example.codechallenge.controller.domain;

import com.example.codechallenge.service.model.Job;
import com.example.codechallenge.service.model.Task;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class JobD {

    @JsonProperty("tasks")
    private List<TaskD> tasks;

    public Job toJob(){


        return new Job.Builder()
                .withTasks(tasks.stream().map(TaskD::toTask).collect(Collectors.toList()))
                .build();
    }
}
