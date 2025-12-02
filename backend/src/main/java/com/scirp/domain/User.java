package com.scirp.domain;

import java.util.UUID;

public class User {
    private final String id;
    private final String username;
    private final UserRole role;
    private final String passwordHash;

    public User(String id, String username, UserRole role, String passwordHash) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.username = username;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}

