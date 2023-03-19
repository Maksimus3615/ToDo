package com.shpp.models;

public class ApiError {
    String error;

    public ApiError(String error) {
        this.error = error;
    }

    public ApiError() {
    }

    public String getError() {
        return error;
    }


    public ApiError setError(String error) {
        this.error = error;
        return this;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "error='" + error + '\'' +
                '}';
    }
}
