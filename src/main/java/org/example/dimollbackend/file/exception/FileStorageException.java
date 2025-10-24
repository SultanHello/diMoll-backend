package org.example.dimollbackend.file.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
    public FileStorageException(String message, Throwable e) { super(message, e);}
}
