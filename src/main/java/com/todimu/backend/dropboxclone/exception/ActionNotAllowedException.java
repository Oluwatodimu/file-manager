package com.todimu.backend.dropboxclone.exception;


public class ActionNotAllowedException extends RuntimeException {

    public ActionNotAllowedException(String message) {
        super(message);
    }
}
