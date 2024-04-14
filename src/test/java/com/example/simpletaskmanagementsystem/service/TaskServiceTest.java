package com.example.simpletaskmanagementsystem.service;

import com.example.simpletaskmanagementsystem.exception.custom.TaskNotFoundException;
import com.example.simpletaskmanagementsystem.model.Task;
import com.example.simpletaskmanagementsystem.repository.TaskRepository;
import com.example.simpletaskmanagementsystem.service.impl.TaskServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_1;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_2;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_DTO_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @DisplayName("Get task by id is OK")
    @Test
    void getTaskByIdIsOk() {
        when(taskRepository.findById(TEST_TASK_1.getId())).thenReturn(Optional.of(TEST_TASK_1));

        assertThat(taskService.getTaskById(TEST_TASK_1.getId())).isEqualTo(TEST_TASK_1);

        verify(taskRepository, times(1)).findById(TEST_TASK_1.getId());
    }

    @DisplayName("Get task by id is fails")
    @Test
    void getTaskByIdIsFails() {
        when(taskRepository.findById(TEST_TASK_1.getId())).thenThrow(new TaskNotFoundException("Task not found"));

        assertThatThrownBy(() -> taskService.getTaskById(TEST_TASK_1.getId()))
                .isInstanceOf(Throwable.class).hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(TEST_TASK_1.getId());
    }

    @DisplayName("Get all tasks return all")
    @Test
    void getAllTasksReturnAll() {
        List<Task> tasks = List.of(TEST_TASK_1, TEST_TASK_2);

        when(taskRepository.findAll()).thenReturn(tasks);

        assertThat(taskService.getAllTasks()).isEqualTo(tasks);

        verify(taskRepository, times(1)).findAll();
    }

    @DisplayName("Get all tasks return 0")
    @Test
    void getAllTasksReturn0() {
        List<Task> tasks = List.of();

        when(taskRepository.findAll()).thenReturn(tasks);

        assertThat(taskService.getAllTasks()).isEqualTo(tasks);

        verify(taskRepository, times(1)).findAll();
    }

    @DisplayName("Create task is OK")
    @Test
    void createTaskIsOk() {
        when(taskRepository.save(any(Task.class))).thenReturn(TEST_TASK_1);

        assertThat(taskService.createTask(TEST_TASK_DTO_1)).isEqualTo(TEST_TASK_1);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @DisplayName("Update task is OK")
    @Test
    void updateTaskIsOk() {
        when(taskRepository.findById(TEST_TASK_1.getId())).thenReturn(Optional.of(TEST_TASK_1));
        when(taskRepository.save(any(Task.class))).thenReturn(TEST_TASK_1);

        assertThat(taskService.updateTask(TEST_TASK_1.getId(), TEST_TASK_DTO_1)).isEqualTo(TEST_TASK_1);

        verify(taskRepository, times(1)).findById(TEST_TASK_1.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @DisplayName("Update task is fails")
    @Test
    void updateTaskIsFails() {
        when(taskRepository.findById(TEST_TASK_1.getId())).thenThrow(new TaskNotFoundException("Task not found"));

        assertThatThrownBy(() -> taskService.updateTask(TEST_TASK_1.getId(), TEST_TASK_DTO_1))
                .isInstanceOf(Throwable.class).hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(TEST_TASK_1.getId());
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @DisplayName("Delete task is OK")
    @Test
    void deleteTaskIsOk() {
        when(taskRepository.findById(TEST_TASK_1.getId())).thenReturn(Optional.of(TEST_TASK_1));
        doNothing().when(taskRepository).delete(TEST_TASK_1);

        assertThatNoException().isThrownBy(() -> taskService.deleteTask(TEST_TASK_1.getId()));

        verify(taskRepository, times(1)).findById(TEST_TASK_1.getId());
        verify(taskRepository, times(1)).delete(TEST_TASK_1);
    }

    @DisplayName("Delete task is fails")
    @Test
    void deleteTaskIsFails() {
        when(taskRepository.findById(TEST_TASK_1.getId())).thenThrow(new TaskNotFoundException("Task not found"));

        assertThatThrownBy(() -> taskService.deleteTask(TEST_TASK_1.getId()))
                .isInstanceOf(Throwable.class).hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(TEST_TASK_1.getId());
        verify(taskRepository, times(0)).delete(TEST_TASK_1);
    }

}