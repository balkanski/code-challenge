package com.example.codechallenge.service.model;

import java.util.List;

public class Task {

    private String name;
    private String shellCommand;
    private List<String> requiredTasks;
    private boolean beingVisited=false;
    private boolean visited=false;

    private Task(String name, String shellCommand, List<String> requiredTasks) {
        this.name = name;
        this.shellCommand = shellCommand;
        this.requiredTasks = requiredTasks;
    }

    public String getName() {
        return name;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public List<String> getRequiredTasks() {
        return requiredTasks;
    }


    public boolean isBeingVisited() {
        return beingVisited;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setBeingVisited(boolean beingVisited) {
        this.beingVisited = beingVisited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public static class Builder{
        private String name;
        private String shellCommand;
        private List<String> requiredTasks;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withShellCommand(String shellCommand){
            this.shellCommand = shellCommand;
            return this;
        }

        public Builder withRequiredTasks(List<String> requiredTasks){
            this.requiredTasks = requiredTasks;
            return this;
        }

        public Task build(){
            return new Task(name, shellCommand, requiredTasks);
        }
    }
}
