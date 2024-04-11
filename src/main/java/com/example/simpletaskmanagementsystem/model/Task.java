package com.example.simpletaskmanagementsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank(message = "Title should not be empty")
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Due date should not be empty")
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed")
    private Boolean completed;
}
