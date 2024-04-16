# Simple task management system

[![build and test](https://github.com/agsamkin/simple-task-management-system/actions/workflows/build.yml/badge.svg)](https://github.com/agsamkin/simple-task-management-system/actions/workflows/build.yml)

Simple task management system using Spring Boot and Spring Data JPA.

API documentation is available by clicking here: [http://host:port/api-doc.html]().

### How to use

#### <u>Get task by id</u>

Request example:

```
GET http://localhost:8080/api/v1/tasks/1
```

Response example:

``` 
{
    "id": 1,
    "title": "foo",
    "description": "foo",
    "dueDate": "2024-04-01T10:11:12",
    "completed": false
}
```

#### <u>Get all tasks</u>

Request example:

```
GET http://localhost:8080/api/v1/tasks
```

Response example:

``` 
[
    {
        "id": 1,
        "title": "foo",
        "description": "foo",
        "dueDate": "2024-04-01T10:11:12",
        "completed": false
    },
    {
        "id": 2,
        "title": "bar",
        "description": "bar",
        "dueDate": "2024-04-03T11:15:20",
        "completed": true
    }
]
```

#### <u>Create new task</u>

Request example:

```
POST http://localhost:8080/api/v1/tasks

{
    "title": "foo",
    "description": "foo",
    "dueDate": "2024-04-01T10:11:12",
    "completed": false
}
```

Response example:

``` 
{
    "id": 1,
    "title": "foo",
    "description": "foo",
    "dueDate": "2024-04-01T10:11:12",
    "completed": false
}
```

#### <u>Update task</u>

Request example:

```
PUT http://localhost:8080/api/v1/tasks/1

{
    "title": "foo",
    "description": "foo",
    "dueDate": "2024-04-05T10:11:12",
    "completed": true
}
```

Response example:

``` 
{
    "id": 1,
    "title": "foo",
    "description": "foo",
    "dueDate": "2024-04-05T10:11:12",
    "completed": true
}
```

#### <u>Delete task</u>

Request example:

```
DELETE http://localhost:8080/api/v1/tasks/1
```
