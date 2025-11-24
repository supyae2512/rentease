package com.rentease.exceptions;

public class ApiError {
    private String timestamp;
    private int status;
    private String error;
    private String path;

    public ApiError(int status, String error, String path) {
        this.timestamp = java.time.OffsetDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.path = path;
    }

}

