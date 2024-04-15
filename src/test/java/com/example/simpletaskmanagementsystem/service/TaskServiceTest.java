package com.example.simpletaskmanagementsystem.service;

import com.example.simpletaskmanagementsystem.dto.TaskDto;
import com.example.simpletaskmanagementsystem.exception.custom.TaskNotFoundException;
import com.example.simpletaskmanagementsystem.model.Task;
import com.example.simpletaskmanagementsystem.repository.TaskRepository;
import com.example.simpletaskmanagementsystem.service.impl.TaskServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_1;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_2;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_DTO_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Captor
    private ArgumentCaptor<Task> argumentCaptor;

    @DisplayName("Get task by id is OK")
    @Test
    void getTaskByIdIsOk() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));

        Task actualTask = taskService.getTaskById(expectedTask.getId());

        assertThat(actualTask).usingRecursiveComparison().isEqualTo(TEST_TASK_1);
        verify(taskRepository).findById(TEST_TASK_1.getId());
    }

    @DisplayName("Get task by id is fails")
    @Test
    void getTaskByIdIsFails() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.findById(expectedTask.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(expectedTask.getId()))
                .isInstanceOf(TaskNotFoundException.class).hasMessage("Task not found");

        verify(taskRepository).findById(expectedTask.getId());
        verifyNoMoreInteractions(taskRepository);
    }

    @DisplayName("Get all tasks return all")
    @Test
    void getAllTasksReturnAll() {
        List<Task> expectedTasks = List.of(TEST_TASK_1, TEST_TASK_2);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks();

        assertThat(actualTasks).usingRecursiveComparison().isEqualTo(expectedTasks);
        verify(taskRepository).findAll();
    }

    @DisplayName("Get all tasks return 0")
    @Test
    void getAllTasksReturn0() {
        List<Task> expectedTasks = List.of();
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks();

        assertThat(actualTasks).usingRecursiveComparison().isEqualTo(expectedTasks);
        verify(taskRepository).findAll();
    }

    @DisplayName("Create task is OK")
    @Test
    void createTaskIsOk() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        TaskDto fromDto = TEST_TASK_DTO_1;
        taskService.createTask(fromDto);

        verify(taskRepository).save(argumentCaptor.capture());
        Task actualTask = argumentCaptor.getValue();
        assertThat(actualTask).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedTask);
    }

    @DisplayName("Update task is OK")
    @Test
    void updateTaskIsOk() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        TaskDto fromDto = TEST_TASK_DTO_1;
        taskService.updateTask(expectedTask.getId(), fromDto);

        verify(taskRepository).findById(expectedTask.getId());
        verify(taskRepository).save(argumentCaptor.capture());
        Task actualTask = argumentCaptor.getValue();
        assertThat(actualTask).usingRecursiveComparison().isEqualTo(expectedTask);
    }

    @DisplayName("Update task is fails")
    @Test
    void updateTaskIsFails() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.findById(expectedTask.getId())).thenReturn(Optional.empty());

        TaskDto fromDto = TEST_TASK_DTO_1;
        assertThatThrownBy(() -> taskService.updateTask(expectedTask.getId(), fromDto))
                .isInstanceOf(TaskNotFoundException.class).hasMessage("Task not found");

        verify(taskRepository).findById(expectedTask.getId());
        verifyNoMoreInteractions(taskRepository);
    }

    @DisplayName("Delete task is OK")
    @Test
    void deleteTaskIsOk() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        doNothing().when(taskRepository).delete(expectedTask);

        taskService.deleteTask(expectedTask.getId());

        verify(taskRepository).findById(expectedTask.getId());
        verify(taskRepository).delete(argumentCaptor.capture());
        Task actualTask = argumentCaptor.getValue();
        assertThat(actualTask).usingRecursiveComparison().isEqualTo(expectedTask);
    }

    @DisplayName("Delete task is fails")
    @Test
    void deleteTaskIsFails() {
        Task expectedTask = TEST_TASK_1;
        when(taskRepository.findById(expectedTask.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(expectedTask.getId()))
                .isInstanceOf(TaskNotFoundException.class).hasMessage("Task not found");

        verify(taskRepository).findById(expectedTask.getId());
        verifyNoMoreInteractions(taskRepository);
    }

}