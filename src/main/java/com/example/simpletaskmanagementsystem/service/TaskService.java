package com.example.simpletaskmanagementsystem.service;

import com.example.simpletaskmanagementsystem.dto.TaskDto;
import com.example.simpletaskmanagementsystem.model.Task;

import java.util.List;

public interface TaskService {
    Task getTaskById(long id);
    List<Task> getAllTasks();

    Task createTask(TaskDto taskDto);
    Task updateTask(long id, TaskDto taskDto);
    void deleteTask(long id);
}
