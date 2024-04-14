package com.example.simpletaskmanagementsystem.service.impl;

import com.example.simpletaskmanagementsystem.dto.TaskDto;
import com.example.simpletaskmanagementsystem.exception.custom.TaskNotFoundException;
import com.example.simpletaskmanagementsystem.model.Task;
import com.example.simpletaskmanagementsystem.repository.TaskRepository;
import com.example.simpletaskmanagementsystem.service.TaskService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(TaskDto taskDto) {
        Task newTask = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .completed(taskDto.getCompleted()).build();

        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        Task task = getTaskById(id);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setCompleted(taskDto.getCompleted());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
}
