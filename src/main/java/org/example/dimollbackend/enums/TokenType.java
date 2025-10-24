package org.example.dimollbackend.enums;

public enum TokenType {
    ACCESS_TOKEN, REFRESH_TOKEN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
