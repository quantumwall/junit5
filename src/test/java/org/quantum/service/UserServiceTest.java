package org.quantum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.quantum.dto.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
	void emptyIfNoUsersAdded() {
		List<User> users = userService.findAll();
		assertTrue(users.isEmpty());
	}

	@Test
	@Order(1)
	void notEmptyIfUsersAdded() {
		userService.add(IVAN);
		userService.add(PETR);
		assertThat(userService.findAll()).hasSize(2);
	}

	@Test
	@Order(2)
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

	@Nested
	@DisplayName("test login functionality")
	class LoginTest {

		@Test
		void shoulThrowExceptionIfUsernameOrPasswordIsNull() {
			assertAll(() -> assertThrows(NullPointerException.class, () -> userService.login(null, "dummy")),
					() -> assertThrows(NullPointerException.class, () -> userService.login("dummy", null)));
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
	}
}
