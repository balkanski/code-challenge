# Code challenge - Job Processor

### Description

The goal of this challenge is to implement an HTTP job processing service.
A job in this case is a collection of tasks, where each task has a name and a shell command.
Tasks may depend on other tasks and require that those are executed beforehand.
The service takes care of sorting the tasks to create a proper execution order.

Here is an example request body:

```json
{
	"tasks": [
		{
			"name": "task-1",
			"command": "touch /tmp/file1"
		},
		{
			"name": "task-2",
			"command": "cat /tmp/file1",
			"requires": [
				"task-3"
				]
		},
		{
			"name": "task-3",
			"command": "echo 'Hello World!' > /tmp/file1",
			"requires": [
				"task-1"
				]
		},
		{
			"name": "task-4",
			"command": "rm /tmp/file1",
			"requires": [
				"task-2",
				"task-3"
				]
		}
		]
}
```
 The example response for this would be: 
 ```json
{
        "tasks": [
            {
                "name": "task-1",
                "command": "touch /tmp/file1",
                "requires": null
            },
            {
                "name": "task-3",
                "command": "echo 'Hello World!' > /tmp/file1",
                "requires": [
                    "task-1"
                ]
            },
            {
                "name": "task-2",
                "command": "cat /tmp/file1",
                "requires": [
                    "task-3"
                ]
            },
            {
                "name": "task-4",
                "command": "rm /tmp/file1",
                "requires": [
                    "task-2",
                    "task-3"
                ]
            }
        ]
    }
```

 Additionally, the service should be able to return a bash script representation directly:
 
```
#!/usr/bin/env bash

touch /tmp/file1
echo 'Hello World!' > /tmp/file1
cat /tmp/file1
rm /tmp/file1
```
### Used technology

This application is written in Java 11 with the use of the Spring Boot framework.
As a build automation system I have used Gradle.
JUnit 5 and Mockito are used for testing.

### How to run
To run the application run the following command:

```
./gradlew bootRun
```

By default the application will run on port 8080

Alternatively, after building the application, you can use the provided docker image by running:

```
     docker build -t cc:latest --rm=true .
     docker run cc -p 8080:8080
```

Both of those will make the application available at:
```
http://localhost:8080
```
To run the tests for the application run:
```
./gradlew test
```

The most recent test coverage is 98%. This can be checked by running:

```
./gradlew jacocoTestReport
```

After executing the command above, the test coverage report can be found at
```
/build/jacocoHtml/index.html
```

### How to use

After starting the application you use your favorite API client or just curl to interact with it.
The two endpoint on it are:
```
/createOrdering
/createReport
``` 

They both require a POST request with a body.

Example usage:

```
curl -d @mytask.json --location --request POST 'http://localhost:8080/createOrdering' --header 'Content-Type: application/json'
curl -d @mytask.json --location --request POST 'http://localhost:8080/createScript' --header 'Content-Type: application/json'
```

Where mytask.json is your input json body.

The output from the /createScript endpoint can be directly piped to bash in the following manner:

```
curl -d @mytask.json --location --request POST 'http://localhost:8080/createScript' --header 'Content-Type: application/json' | bash
```

### Possible improvements
I have identified a bottleneck in the JobProcessorService class that will significantly increase the time complexity.
This was due to flawed design of the Task class and can be improved in future versions.
Also, another possible improvement is to check whether a task with the same name is already created for the job, 
which should not be allowed but currently is.
