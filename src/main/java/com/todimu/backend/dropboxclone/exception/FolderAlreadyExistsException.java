package com.todimu.backend.dropboxclone.exception;

public class FolderAlreadyExistsException extends RuntimeException {

    public FolderAlreadyExistsException(String message) {
        super(message);
    }
}
