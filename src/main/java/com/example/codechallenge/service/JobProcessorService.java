package com.example.codechallenge.service;

import com.example.codechallenge.service.exception.UnableToEvaluateException;
import com.example.codechallenge.service.exception.UnrecognisedRequiredTask;
import com.example.codechallenge.service.model.Job;
import com.example.codechallenge.service.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobProcessorService {

    private String script;

    public String createScript(Job job) {

        script = "#!/usr/bin/env bash\n\n";

        if(hasCycle(job)){
            return "The provided input contains loops and order cannot be defined";
        } else {
            return script;
        }
    }

    private boolean hasCycle(Job job) {
        for (Task t: job.getTasks()) {
            if (!t.isVisited() && hasCycle(job, t)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycle(Job job, Task currentTask) {
        currentTask.setBeingVisited(true);

        if(currentTask.getRequiredTasks()!=null) {

            List<Task> requiredTasks = getRequiredTasks(job, currentTask);

            for (Task neighbor : requiredTasks) {
                if (neighbor.isBeingVisited()) {
                    // backward edge exists
                    return true;
                } else if (!neighbor.isVisited() && hasCycle(job, neighbor)) {
                    return true;
                }
            }
        }
        currentTask.setBeingVisited(false);
        currentTask.setVisited(true);
        script+=String.format("%s\n", currentTask.getShellCommand());
        return false;
    }

    private List<Task> getRequiredTasks(Job job, Task task){
        //I know that this will make the application significantly slower,
        //but this was implemented after the model, so I decided to leave it
        //as it is and propose as future improvement to make the check for a valid
        //required task when transforming from domain object.

        task.getRequiredTasks().stream().forEach(x -> {
            //check if the task exists
            if( !job.getTasks().stream().map(y -> y.getName()).collect(Collectors.toList()).contains(x)){
                throw new UnrecognisedRequiredTask(String.format("%s is not a recognised task", x));
            }
        });
       return job.getTasks().stream().filter(x -> task.getRequiredTasks().contains(x.getName())).collect(Collectors.toList());
    }
}