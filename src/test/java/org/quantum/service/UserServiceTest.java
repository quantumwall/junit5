package org.quantum.service;

import org.junit.jupiter.api.*;
import org.quantum.dto.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @BeforeAll
    void beforeAll() {
        System.out.println("Before all " + this);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each " + this);
    }

    @Test
    void emptyIfNoUsersAdded() {
        var userService = new UserService();
        List<User> users = userService.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void notEmptyIfUsersAdded() {
        var userService = new UserService();
        userService.add(new User());
        userService.add(new User());
        assertEquals(2, userService.findAll().size());
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each " + this);
    }

    @AfterAll
    void afterAll() {
        System.out.println("After all " + this);
    }
}

