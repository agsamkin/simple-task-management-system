package com.example.simpletaskmanagementsystem.util;

import com.example.simpletaskmanagementsystem.dto.TaskDto;
import com.example.simpletaskmanagementsystem.model.Task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestUtil {
    public final static String BASE_URL = "/api/v1";

    public static final TaskDto TEST_TASK_DTO_1 =
            TaskDto.builder().title("title1")
                    .description("description1")
                    .dueDate(LocalDateTime.of(2024, 4, 1, 9, 10, 15))
                    .completed(false).build();

    public static final Task TEST_TASK_1 =
            Task.builder().id(1L)
                    .title("title1")
                    .description("description1")
                    .dueDate(LocalDateTime.of(2024, 4, 1, 9, 10, 15))
                    .completed(false).build();

    public static final TaskDto TEST_TASK_DTO_2 =
            TaskDto.builder().title("title2")
                    .description("description2")
                    .dueDate(LocalDateTime.of(2024, 4, 3, 10, 15, 20))
                    .completed(true).build();

    public static final Task TEST_TASK_2 =
            Task.builder().id(2L)
                    .title("title2")
                    .description("description2")
                    .dueDate(LocalDateTime.of(2024, 4, 3, 10, 15, 20))
                    .completed(true).build();

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
