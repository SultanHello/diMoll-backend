package org.example.dimollbackend.exeption;

public class NotFoundUserRole extends RuntimeException {
    public NotFoundUserRole(String message) {
        super(message);
    }
}
