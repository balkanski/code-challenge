package com.example.codechallenge.integration;

import com.example.codechallenge.CoreApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CoreApplication.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    String validScript = "#!/usr/bin/env bash\n" +
            "\n" +
            "touch /tmp/file1\n" +
            "echo 'Hello World!' > /tmp/file1\n" +
            "cat /tmp/file1\n" +
            "rm /tmp/file1\n";

    String noLoopRequest = "{\n" +
            "\"tasks\": [\n" +
            "{\n" +
            "\"name\": \"task-1\",\n" +
            "\"command\": \"touch /tmp/file1\"\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"task-2\",\n" +
            "\"command\": \"cat /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-3\"\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"task-3\",\n" +
            "\"command\": \"echo 'Hello World!' > /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-1\"\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"task-4\",\n" +
            "\"command\": \"rm /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-2\",\n" +
            "\"task-3\"\n" +
            "]\n" +
            "}\n" +
            "]\n" +
            "}";

    String invalidRequiredTask = "{\n" +
            "\"tasks\": [\n" +
            "{\n" +
            "\"name\": \"task-1\",\n" +
            "\"command\": \"touch /tmp/file1\"\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"task-2\",\n" +
            "\"command\": \"cat /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-3\"\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"task-3\",\n" +
            "\"command\": \"echo 'Hello World!' > /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-1\"\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "\"name\": \"task-4\",\n" +
            "\"command\": \"rm /tmp/file1\",\n" +
            "\"requires\": [\n" +
            "\"task-2\",\n" +
            "\"task-3\",\n" +
            "\"task-5\"\n" +
            "]\n" +
            "}\n" +
            "]\n" +
            "}";

    @Test
    public void whenCreateScript_thenReturnOrderedListOfTasks() throws Exception {
        mockMvc.perform(post("/createOrdering").contentType("application/json").content(noLoopRequest))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"tasks\": [\n" +
                        "        {\n" +
                        "            \"name\": \"task-1\",\n" +
                        "            \"command\": \"touch /tmp/file1\",\n" +
                        "            \"requires\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"name\": \"task-3\",\n" +
                        "            \"command\": \"echo 'Hello World!' > /tmp/file1\",\n" +
                        "            \"requires\": [\n" +
                        "                \"task-1\"\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"name\": \"task-2\",\n" +
                        "            \"command\": \"cat /tmp/file1\",\n" +
                        "            \"requires\": [\n" +
                        "                \"task-3\"\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"name\": \"task-4\",\n" +
                        "            \"command\": \"rm /tmp/file1\",\n" +
                        "            \"requires\": [\n" +
                        "                \"task-2\",\n" +
                        "                \"task-3\"\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}"));
    }

    @Test
    public void whenCreateScript_thenReturnOrderedScript() throws Exception {
        mockMvc.perform(post("/createScript").contentType("application/json").content(noLoopRequest))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(validScript));
    }

    @Test
    public void whenCreateScript_thenBadRequest() throws Exception {
        mockMvc.perform(post("/createScript").contentType("application/json").content(invalidRequiredTask))
                .andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    public void whenCreateOrdering_thenBadRequest() throws Exception {
        mockMvc.perform(post("/createOrdering").contentType("application/json").content(invalidRequiredTask))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
