package com.example.codechallenge.controller;

import com.example.codechallenge.controller.domain.JobD;
import com.example.codechallenge.service.JobProcessorService;
import com.example.codechallenge.service.model.Job;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobProcessorControllerTest {

    @Mock
    JobProcessorService jobProcessorService;

    @InjectMocks
    JobProcessorController jobProcessorController;

    String jobJson = "{\"tasks\":" +
            "["+
            "{\n" +
            "\"name\": \"task-4\",\n" +
            "\"command\": \"rm /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-2\",\n" +
            "\"task-3\"\n" +
            "]\n" +
            "}]}";

    ObjectMapper mapper = new ObjectMapper();

    JobD testJob;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        testJob = mapper.readValue(jobJson, JobD.class);
    }

    @Test
    void createScript() throws JsonProcessingException {





        ArgumentCaptor<Job> captor = ArgumentCaptor.forClass(Job.class);

        when(jobProcessorService.createScript(any(Job.class))).thenReturn("Execute this");

        ResponseEntity responseEntity = jobProcessorController.createScript(testJob);

        verify(jobProcessorService, times(1)).createScript(captor.capture());
        Job createdJob = captor.getValue();

        assertThat(createdJob.getTasks().size()).isEqualTo(1);
        assertThat(createdJob.getTasks().get(0).getName()).isEqualTo("task-4");
        assertThat(createdJob.getTasks().get(0).getShellCommand()).isEqualTo("rm /tmp/file1");
        assertThat(createdJob.getTasks().get(0).getRequiredTasks().size()).isEqualTo(2);
        assertThat(createdJob.getTasks().get(0).getRequiredTasks().contains("task-2")).isEqualTo(true);
        assertThat(createdJob.getTasks().get(0).getRequiredTasks().contains("task-3")).isEqualTo(true);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Execute this");
    }

    @Test
    void createScript_exception(){

        doThrow(new NullPointerException()).when(jobProcessorService).createScript(any(Job.class));

        ResponseEntity responseEntity = jobProcessorController.createScript(testJob);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

}
