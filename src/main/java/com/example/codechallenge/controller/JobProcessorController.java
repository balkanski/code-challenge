package com.example.codechallenge.controller;

import com.example.codechallenge.service.JobProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.codechallenge.controller.domain.JobD;

@RestController
public class JobProcessorController {

    @Autowired
    JobProcessorService jobProcessorService;

    @PostMapping(value = "/createScript", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createScript(@RequestBody JobD job) {

        try {
            return new ResponseEntity(jobProcessorService.createScript(job.toJob()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}

