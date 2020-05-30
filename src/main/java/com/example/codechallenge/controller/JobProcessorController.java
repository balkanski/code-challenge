package com.example.codechallenge.controller;

import com.example.codechallenge.service.JobProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(JobProcessorController.class);

    @PostMapping(value = "/createScript", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createScript(@RequestBody JobD job) {

        try {
            return new ResponseEntity(jobProcessorService.createScript(job.toJob()), HttpStatus.OK);
        } catch (Exception e){
            logger.error("Error while creating script", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/createOrdering", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOrdering(@RequestBody JobD job){
        try {
            return new ResponseEntity(JobD.fromDomain(jobProcessorService.createOrdering(job.toJob())), HttpStatus.OK);
        } catch (Exception e){
            logger.error("Error while creating ordering", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}

