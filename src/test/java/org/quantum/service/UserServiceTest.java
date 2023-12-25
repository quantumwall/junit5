package org.quantum.service;

import org.junit.jupiter.api.*;
import org.quantum.dto.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private final UserService userService;
    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");

    public UserServiceTest() {
	this.userService = new UserService();
    }

    @BeforeAll
    void beforeAll() {
    }

    @BeforeEach
    void beforeEach() {
	userService.deleteAll();
    }

    @Test
    void loginSuccessIfUserExists() {
	userService.add(IVAN);
	var optUser = userService.login(IVAN.getUsername(), IVAN.getPassword());
	assertTrue(optUser.isPresent());
    }

    @Test
    void loginFailIfUserIsNotExists() {
	userService.add(IVAN);
	var optUser = userService.login("dummy", IVAN.getPassword());
	assertThat(optUser).isEmpty();
    }

    @Test
    void emptyIfNoUsersAdded() {
	List<User> users = userService.findAll();
	assertTrue(users.isEmpty());
    }

    @Test
    void notEmptyIfUsersAdded() {
	userService.add(IVAN);
	userService.add(PETR);
	assertThat(userService.findAll()).hasSize(2);
    }

    @Test
    void testConvertedById() {
	userService.add(IVAN, PETR);
	var users = userService.getUsersConvertedById();
	assertAll(() -> assertThat(users).containsKeys(1, 2), () -> assertThat(users).containsValues(IVAN, PETR));
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
