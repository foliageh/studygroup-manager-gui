package com.labproject.exceptions;

public class AuthenticationFailed extends ServerException {
    public AuthenticationFailed(String errorMessage) {
        super(errorMessage);
    }
}
