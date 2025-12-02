package com.scirp.repository;

import com.scirp.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class UserRepository {
    private final List<User> users = new CopyOnWriteArrayList<>();

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public void seed(List<User> seedUsers) {
        users.clear();
        users.addAll(seedUsers);
    }
}

