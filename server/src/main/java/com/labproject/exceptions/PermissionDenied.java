package com.labproject.exceptions;

public class PermissionDenied extends ServerException {
    public PermissionDenied(String errorMessage) {
        super(errorMessage);
    }
}
