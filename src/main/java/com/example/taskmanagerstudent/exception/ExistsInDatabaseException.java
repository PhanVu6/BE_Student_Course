package com.example.taskmanagerstudent.exception;

public class ExistsInDatabaseException extends RuntimeException {
    public ExistsInDatabaseException(String message) {
        super(message);
    }
}