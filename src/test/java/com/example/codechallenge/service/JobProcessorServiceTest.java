package com.example.codechallenge.service;

import com.example.codechallenge.service.exception.UnableToEvaluateException;
import com.example.codechallenge.service.exception.UnrecognisedRequiredTask;
import com.example.codechallenge.service.model.Job;
import com.example.codechallenge.service.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JobProcessorService.class})
class JobProcessorServiceTest {

    @Autowired
    JobProcessorService jobProcessorService;

    private Task t1 = new Task.Builder()
            .withName("task-1")
            .withShellCommand("touch /tmp/file1")
            .build();

    private Task t2 = new Task.Builder()
            .withName("task-2")
            .withShellCommand("cat /tmp/file1")
            .withRequiredTasks(Arrays.asList("task-3"))
            .build();

    private Task t3 = new Task.Builder()
            .withName("task-3")
            .withShellCommand("echo 'Hello World!' > /tmp/file1")
            .withRequiredTasks(Arrays.asList("task-1"))
            .build();

    private Task t4 = new Task.Builder()
            .withName("task-4")
            .withShellCommand("rm /tmp/file1")
            .withRequiredTasks(Arrays.asList("task-2", "task-3"))
            .build();


    private Task t5 = new Task.Builder()
            .withName("task-5")
            .withShellCommand("ls")
            .withRequiredTasks(Arrays.asList("task-2", "task-5"))
            .build();

    private Task t6 = new Task.Builder()
            .withName("task-1")
            .withShellCommand("touch /tmp/file1")
            .withRequiredTasks(Arrays.asList("task-3"))
            .build();


    @Test
    void createScript_caseNoLoop() {

        Job job = new Job.Builder().withTasks(Arrays.asList(t1, t2, t3, t4)).build();

        String expectedOutput = "#!/usr/bin/env bash\n" +
                "\n" +
                "touch /tmp/file1\n" +
                "echo 'Hello World!' > /tmp/file1\n" +
                "cat /tmp/file1\n" +
                "rm /tmp/file1\n";

        assertThat(jobProcessorService.createScript(job)).isEqualTo(expectedOutput);
    }

    @Test
    void createScript_caseSelfLoop(){
        Job job = new Job.Builder().withTasks(Arrays.asList(t1, t2, t3, t5)).build();

        UnableToEvaluateException exception = assertThrows(UnableToEvaluateException.class, () -> {
            jobProcessorService.createScript(job);
        });

        String expectedMessage = "This may be due to existence of loops in the order of commands";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);

    }

    @Test
    void createScript_caseGeneralLoop(){

        Job job = new Job.Builder().withTasks(Arrays.asList(t2, t3, t6)).build();

        UnableToEvaluateException exception = assertThrows(UnableToEvaluateException.class, () -> {
            jobProcessorService.createScript(job);
        });

        String expectedMessage = "This may be due to existence of loops in the order of commands";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void createScript_caseException(){
        Job job = new Job.Builder().withTasks(Arrays.asList(t1, t2, t4)).build();

        UnrecognisedRequiredTask exception = assertThrows(UnrecognisedRequiredTask.class, () -> {
            jobProcessorService.createScript(job);
        });

        String expectedMessage = "task-3 is not a recognised task";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
