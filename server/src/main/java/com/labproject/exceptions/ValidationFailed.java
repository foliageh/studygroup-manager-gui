package com.labproject.exceptions;

public class ValidationFailed extends ServerException {
    public ValidationFailed(String errorMessage) {
        super(errorMessage);
    }
}
