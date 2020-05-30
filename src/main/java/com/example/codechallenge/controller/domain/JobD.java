package com.example.codechallenge.controller.domain;

import com.example.codechallenge.service.model.Job;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class JobD {

    @JsonProperty("tasks")
    private List<TaskD> tasks;

    public JobD(List<TaskD> tasks) {
        this.tasks = tasks;
    }

    public JobD(){ }

    public Job toJob(){
        return new Job.Builder()
                .withTasks(tasks.stream().map(TaskD::toTask).collect(Collectors.toList()))
                .build();
    }

    public static JobD fromDomain(Job j){

        return new JobD(j.getTasks().stream().map(TaskD::fromDomain).collect(Collectors.toList()));

    }
}
