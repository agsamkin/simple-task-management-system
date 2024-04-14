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
import static org.mockito.Mockito.times;
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
        when(taskService.getTaskById(TEST_TASK_1.getId())).thenReturn(TEST_TASK_1);

        mockMvc.perform(
                get(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID
                        , TEST_TASK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_TASK_1.getId()))
                .andExpect(jsonPath("$.title").value(TEST_TASK_1.getTitle()));

        verify(taskService, times(1)).getTaskById(TEST_TASK_1.getId());
    }

    @DisplayName("Get task by id is fails")
    @Test
    void getByIdIsFails() throws Exception {
        when(taskService.getTaskById(TEST_TASK_1.getId())).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(get(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, TEST_TASK_1.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("Task not found", result.getResolvedException().getMessage()));

        verify(taskService, times(1)).getTaskById(TEST_TASK_1.getId());
    }

    @DisplayName("Get all tasks return all")
    @Test
    void getAllReturnAll() throws Exception {
        List<Task> tasks = List.of(TEST_TASK_1, TestUtil.TEST_TASK_2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get(BASE_URL + TaskController.TASK_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));

        verify(taskService, times(1)).getAllTasks();
    }

    @DisplayName("Get all tasks return 0")
    @Test
    void getAllReturn0() throws Exception {
        List<Task> tasks = List.of();

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get(BASE_URL + TaskController.TASK_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));

        verify(taskService, times(1)).getAllTasks();
    }

    @DisplayName("Create task is OK")
    @Test
    void createIsOk() throws Exception {
        when(taskService.createTask(any(TaskDto.class))).thenReturn(TEST_TASK_1);

        mockMvc.perform(post(BASE_URL + TASK_CONTROLLER_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_TASK_DTO_1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_TASK_1.getId()))
                .andExpect(jsonPath("$.title").value(TEST_TASK_1.getTitle()));

        verify(taskService, times(1)).createTask(any(TaskDto.class));
    }

    @DisplayName("Update task is OK")
    @Test
    void updateIsOk() throws Exception {
        when(taskService.updateTask(any(Long.class), any(TaskDto.class))).thenReturn(TEST_TASK_2);

        mockMvc.perform(put(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID, TEST_TASK_1.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_TASK_DTO_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TEST_TASK_2.getTitle()))
                .andExpect(jsonPath("$.description").value(TEST_TASK_2.getDescription()));

        verify(taskService, times(1)).updateTask(any(Long.class), any(TaskDto.class));
    }

    @DisplayName("Update task is fails")
    @Test
    void updateIsFails() throws Exception {
        when(taskService.updateTask(any(Long.class), any(TaskDto.class))).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(put(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID,
                        TEST_TASK_1.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_TASK_DTO_2)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("Task not found", result.getResolvedException().getMessage()));

        verify(taskService, times(1)).updateTask(any(Long.class), any(TaskDto.class));
    }

    @DisplayName("Delete task is OK")
    @Test
    void deleteIsOk() throws Exception {
        doNothing().when(taskService).deleteTask(TEST_TASK_1.getId());

        mockMvc.perform(delete(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID,
                        TEST_TASK_1.getId()))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(TEST_TASK_1.getId());
    }

    @DisplayName("Delete task is fails")
    @Test
    void deleteIsFails() throws Exception {
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(any(Long.class));

        mockMvc.perform(delete(BASE_URL + TASK_CONTROLLER_PATH + TaskController.ID,
                        TEST_TASK_1.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(result -> assertEquals("Task not found", result.getResolvedException().getMessage()));

        verify(taskService, times(1)).deleteTask(TEST_TASK_1.getId());
    }

}