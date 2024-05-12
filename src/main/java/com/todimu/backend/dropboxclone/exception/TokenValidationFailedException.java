package com.todimu.backend.dropboxclone.exception;

public class TokenValidationFailedException extends RuntimeException {

    public TokenValidationFailedException(String message) {
        super(message);
    }
}
