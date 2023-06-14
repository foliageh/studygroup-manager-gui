package com.labproject.serialization.exceptions;

public class FieldNullValueException extends FieldValidationException {
    public FieldNullValueException(String errorMessage) {
        super(errorMessage);
    }
}
