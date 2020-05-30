package com.example.codechallenge.controller.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class JobDTest {

    @Test
    public void toJob() throws JsonProcessingException {

        String jobJson = "{\"tasks\":" +
                "[{\n" +
                "\"name\": \"task-3\",\n" +
                "\"command\": \"echo 'Hello World!' > /tmp/file1\",\n" +
                "\"requires\": [\n" +
                "\"task-1\"\n" +
                "]\n" +
                "}," +
                "{\n" +
                "\"name\": \"task-4\",\n" +
                "\"command\": \"rm /tmp/file1\",\n" +
                "\"requires\": [\n" +
                "\"task-2\",\n" +
                "\"task-3\"\n" +
                "]\n" +
                "}]}";

        ObjectMapper mapper = new ObjectMapper();
        JobD testJob = mapper.readValue(jobJson, JobD.class);

        assertThat(testJob.toJob().getTasks().size()).isEqualTo(2);
    }
}
