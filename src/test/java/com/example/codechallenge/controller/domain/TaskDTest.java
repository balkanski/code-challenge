package com.example.codechallenge.controller.domain;

import com.example.codechallenge.service.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class TaskDTest {

    @Test
    void toTask() throws JsonProcessingException {
        String taskJson = "{\n" +
                "\"name\": \"task-4\",\n" +
                "\"command\": \"rm /tmp/file1\",\n" +
                "\"requires\": [\n" +
                "\"task-2\",\n" +
                "\"task-3\"\n" +
                "]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        TaskD testTask = mapper.readValue(taskJson, TaskD.class);

        Task convertedTask = testTask.toTask();

        assertThat(convertedTask.getName()).isEqualTo("task-4");
        assertThat(convertedTask.getShellCommand()).isEqualTo("rm /tmp/file1");
        assertThat(convertedTask.getRequiredTasks().size()).isEqualTo(2);
        assertThat(convertedTask.getRequiredTasks().contains("task-2")).isEqualTo(true);
        assertThat(convertedTask.getRequiredTasks().contains("task-3")).isEqualTo(true);
    }
}
