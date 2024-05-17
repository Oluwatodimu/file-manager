package com.todimu.backend.dropboxclone.exception;

public class FileAlreadyExistsException extends RuntimeException {

    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
