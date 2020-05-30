package com.example.codechallenge.controller.domain;

import com.example.codechallenge.service.model.Task;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TaskD {

    @JsonProperty("name")
    private String name;
    @JsonProperty("command")
    private String shellCommand;
    @JsonProperty("requires")
    private List<String> requiredTasks;

    public Task toTask(){
        return new Task.Builder()
                .withName(name)
                .withShellCommand(shellCommand)
                .withRequiredTasks(requiredTasks)
                .build();
    }
}
