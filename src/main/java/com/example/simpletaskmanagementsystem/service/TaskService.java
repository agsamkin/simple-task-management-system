package com.example.simpletaskmanagementsystem.service;

import com.example.simpletaskmanagementsystem.dto.TaskDto;
import com.example.simpletaskmanagementsystem.model.Task;

import com.querydsl.core.types.Predicate;

import java.util.List;

public interface TaskService {
    Task getTaskById(long id);
    List<Task> getAllTasks();
    List<Task> getAllTasks(Predicate predicate);

    Task createTask(TaskDto taskDto);
    Task updateTask(long id, TaskDto taskDto);
    void deleteTask(long id);
}
