package org.example.dimollbackend.exeption;

public class NotOwnerException extends RuntimeException{
    public NotOwnerException(String message) {
        super(message);
    }
}
