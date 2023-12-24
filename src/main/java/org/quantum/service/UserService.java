package org.quantum.service;

import org.quantum.dto.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return List.copyOf(users);
    }

    public void add(User user) {
        users.add(user);
    }
}
