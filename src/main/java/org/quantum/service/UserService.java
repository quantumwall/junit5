package org.quantum.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quantum.dto.User;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> findAll() {
	return List.copyOf(users);
    }

    public void add(User... user) {
	users.addAll(Arrays.asList(user));
    }

    public void deleteAll() {
	users.clear();
    }

    public Optional<User> login(String username, String password) {
	return users.stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
		.findFirst();
    }

    public Map<Integer, User> getUsersConvertedById() {
	return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
