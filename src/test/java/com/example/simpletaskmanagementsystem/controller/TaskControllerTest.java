package com.example.simpletaskmanagementsystem.controller;

import com.example.simpletaskmanagementsystem.dto.TaskDto;
import com.example.simpletaskmanagementsystem.exception.custom.TaskNotFoundException;
import com.example.simpletaskmanagementsystem.model.Task;
import com.example.simpletaskmanagementsystem.service.impl.TaskServiceImpl;
import com.example.simpletaskmanagementsystem.util.TestUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.simpletaskmanagementsystem.controller.TaskController.TASK_CONTROLLER_PATH;
import static com.example.simpletaskmanagementsystem.util.TestUtil.BASE_URL;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_1;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_2;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_DTO_1;
import static com.example.simpletaskmanagementsystem.util.TestUtil.TEST_TASK_DTO_2;

import static org.hamcrest.Matchers.hasSize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @MockBean
    private TaskServiceImpl taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Get task by id is OK")
    @Test
    void getByIdIsOk() throws Exception {
        Task expectedTask = TEST_TASK_1;
        when(taskService.getTaskById(expectedTask.getId())).thenReturn(expectedTask);

        mockMvc.perform(
                get(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID
                        , expectedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTask.getId()))
                .andExpect(jsonPath("$.title").value(expectedTask.getTitle()));

        verify(taskService).getTaskById(expectedTask.getId());
    }

    @DisplayName("Get task by id is fails")
    @Test
    void getByIdIsFails() throws Exception {
        Task expectedTask = TEST_TASK_1;
        when(taskService.getTaskById(expectedTask.getId())).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(get(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, expectedTask.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("Task not found", result.getResolvedException().getMessage()));

        verify(taskService).getTaskById(expectedTask.getId());
    }

    @DisplayName("Get all tasks return all")
    @Test
    void getAllReturnAll() throws Exception {
        List<Task> expectedTasks = List.of(TEST_TASK_1, TestUtil.TEST_TASK_2);
        when(taskService.getAllTasks()).thenReturn(expectedTasks);

        mockMvc.perform(get(BASE_URL + TaskController.TASK_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));

        verify(taskService).getAllTasks();
    }

    @DisplayName("Get all tasks return 0")
    @Test
    void getAllReturn0() throws Exception {
        List<Task> expectedTasks = List.of();
        when(taskService.getAllTasks()).thenReturn(expectedTasks);

        mockMvc.perform(get(BASE_URL + TaskController.TASK_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));

        verify(taskService).getAllTasks();
    }

    @DisplayName("Create task is OK")
    @Test
    void createIsOk() throws Exception {
        Task expectedTask = TEST_TASK_1;
        when(taskService.createTask(any(TaskDto.class))).thenReturn(expectedTask);

        TaskDto fromDto = TEST_TASK_DTO_1;
        mockMvc.perform(post(BASE_URL + TASK_CONTROLLER_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fromDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_TASK_1.getId()))
                .andExpect(jsonPath("$.title").value(TEST_TASK_1.getTitle()));

        verify(taskService).createTask(any(TaskDto.class));
    }

    @DisplayName("Update task is OK")
    @Test
    void updateIsOk() throws Exception {
        Task expectedTask = TEST_TASK_2;
        when(taskService.updateTask(any(Long.class), any(TaskDto.class))).thenReturn(expectedTask);

        long updateId = TEST_TASK_1.getId();
        TaskDto fromDto = TEST_TASK_DTO_2;
        mockMvc.perform(put(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, updateId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fromDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedTask.getTitle()))
                .andExpect(jsonPath("$.description").value(expectedTask.getDescription()));

        verify(taskService).updateTask(any(Long.class), any(TaskDto.class));
    }

    @DisplayName("Update task is fails")
    @Test
    void updateIsFails() throws Exception {
        when(taskService.updateTask(any(Long.class), any(TaskDto.class))).thenThrow(new TaskNotFoundException("Task not found"));

        long updateId = TEST_TASK_1.getId();
        TaskDto fromDto = TEST_TASK_DTO_2;
        mockMvc.perform(put(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, updateId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fromDto)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("Task not found", result.getResolvedException().getMessage()));

        verify(taskService).updateTask(any(Long.class), any(TaskDto.class));
    }

    @DisplayName("Delete task is OK")
    @Test
    void deleteIsOk() throws Exception {
        Task expectedTask = TEST_TASK_1;
        doNothing().when(taskService).deleteTask(expectedTask.getId());

        mockMvc.perform(delete(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, expectedTask.getId()))
                .andExpect(status().isOk());

        verify(taskService).deleteTask(expectedTask.getId());
    }

    @DisplayName("Delete task is fails")
    @Test
    void deleteIsFails() throws Exception {
        Task expectedTask = TEST_TASK_1;
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(any(Long.class));

        mockMvc.perform(delete(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, expectedTask.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("Task not found", result.getResolvedException().getMessage()));

        verify(taskService).deleteTask(expectedTask.getId());
    }

}