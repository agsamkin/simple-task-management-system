package com.example.simpletaskmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    @NotBlank(message = "Title should not be empty")
    private String title;

    private String description;

    @NotNull(message = "Due date should not be empty")
    private LocalDateTime dueDate;

    private Boolean completed;
}
