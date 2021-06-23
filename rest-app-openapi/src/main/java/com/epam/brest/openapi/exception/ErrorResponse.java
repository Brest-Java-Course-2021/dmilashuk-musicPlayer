package com.epam.brest.openapi.exception;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {

    private List<String> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public ErrorResponse(String ... message) {
        this.errors = Arrays.asList(message);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
