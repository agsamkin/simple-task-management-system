package com.example.simpletaskmanagementsystem.exception;

import com.example.simpletaskmanagementsystem.exception.custom.TaskNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseBody
@RestControllerAdvice
public class BaseExceptionHandler {
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse generalExceptionHandler(Exception exception) {
        return getErrorResponse(exception.getMessage());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse taskNotFoundExceptionHandler(TaskNotFoundException exception) {
        return getErrorResponse(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse validationExceptionsHandler(Exception exception) {
        return getErrorResponse(exception.getMessage());
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse validationExceptionsHandler(MethodArgumentNotValidException exception) {
        return getErrorResponse(exception.getMessage());
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse validationExceptionsHandler(DataIntegrityViolationException exception) {
        return getErrorResponse(exception.getCause().getCause().getMessage());
    }

    private ErrorResponse getErrorResponse(String message) {
        ErrorResponse errorResponse = new ErrorResponse(message);
        return errorResponse;
    }
}
