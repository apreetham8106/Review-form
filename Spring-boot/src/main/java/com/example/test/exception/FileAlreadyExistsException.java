package com.example.test.exception;

public class FileAlreadyExistsException extends RuntimeException{
    public FileAlreadyExistsException(String message) {
        super(message);
    }

}
