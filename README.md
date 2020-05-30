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
 The example response fot this would be: 
 
 Additionally, the service should be able to return a bash script representation directly:
 
```
#!/usr/bin/env bash

touch /tmp/file1
echo 'Hello World!' > /tmp/file1
cat /tmp/file1
rm /tmp/file1
```
