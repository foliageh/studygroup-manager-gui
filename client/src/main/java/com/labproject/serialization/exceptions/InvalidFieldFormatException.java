package com.labproject.serialization.exceptions;

public class InvalidFieldFormatException extends FieldValidationException {
    public InvalidFieldFormatException(String errorMessage) {
        super(errorMessage);
    }
}
