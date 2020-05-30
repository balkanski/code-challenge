package com.example.codechallenge.service.model;

import java.util.ArrayList;
import java.util.List;

public class Job {

    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public Job(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addToTasks(Task t){
        if(this.getTasks()==null){
            this.tasks = new ArrayList<Task>();
        }
        this.tasks.add(t);
    }

    public static class Builder{
        private List<Task> tasks;

        public Builder withTasks(List<Task> tasks){
            this.tasks = tasks;
            return this;
        }

        public Job build(){
            return new Job(tasks);
        }
    }
}
