package com.ipms.policy.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private HttpStatus status;
    private int statusCode;
    private String message;
    private long timestamp;

    public ErrorResponse(HttpStatus status, int statusCode, String message, long timestamp) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
