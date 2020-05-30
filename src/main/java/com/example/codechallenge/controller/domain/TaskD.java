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

    public TaskD(String name, String shellCommand, List<String> requiredTasks) {
        this.name = name;
        this.shellCommand = shellCommand;
        this.requiredTasks = requiredTasks;
    }

    public TaskD(){}

    public Task toTask(){
        return new Task.Builder()
                .withName(name)
                .withShellCommand(shellCommand)
                .withRequiredTasks(requiredTasks)
                .build();
    }

    public static TaskD fromDomain (Task t){
        return new TaskD(t.getName(), t.getShellCommand(), t.getRequiredTasks());
    }
}
