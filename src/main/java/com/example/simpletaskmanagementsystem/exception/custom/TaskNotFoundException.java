package com.example.simpletaskmanagementsystem.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
