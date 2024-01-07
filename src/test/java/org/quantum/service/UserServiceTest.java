package org.quantum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.only;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.quantum.dao.UserDao;
import org.quantum.dto.User;
import org.quantum.paramresolver.UserServiceParamResolver;

@ExtendWith({ UserServiceParamResolver.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

	private UserService userService;
	private UserDao userDao;
	private static final User IVAN = User.of(1, "Ivan", "123");
	private static final User PETR = User.of(2, "Petr", "111");

	@BeforeAll
	void beforeAll() {
	}

	@BeforeEach
	void beforeEach() {
		userDao = Mockito.spy(UserDao.class);
		userService = new UserService(userDao);
	}

	@Test
	void deleteTest() throws SQLException {
		userService.add(IVAN);
		Mockito.doReturn(true).when(userDao).delete(IVAN.getId());
//		Mockito.when(userDao.delete(IVAN.getId())).thenReturn(true);
		assertThat(userService.delete(IVAN.getId())).isTrue();
		var argumentCaptor = ArgumentCaptor.forClass(Integer.class);
		Mockito.verify(userDao, only()).delete(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue()).isEqualTo(IVAN.getId());
	}

	@Test
	@Disabled("flaky, check later")
	void emptyIfNoUsersAdded() {
		List<User> users = userService.findAll();
		assertTrue(users.isEmpty());
	}

	@Test
	@Order(1)
	@Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
	void notEmptyIfUsersAdded() {
		userService.add(IVAN);
		userService.add(PETR);
//		try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			System.err.println("Timeout... interrupted");
//		}
		assertThat(userService.findAll()).hasSize(2);
	}

	@Order(2)
	@RepeatedTest(5)
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

	/*
	 * static method used in parameterized tests, provide 2 group of 2 arguments
	 */
	static Stream<Arguments> incorrectUsernamePasswordProvider() {
		return Stream.of(Arguments.of(null, "dummy"), Arguments.of("dummy", null));
	}

	@Nested
	@DisplayName("test login functionality")
	class LoginTest {

		@MethodSource("org.quantum.service.UserServiceTest#incorrectUsernamePasswordProvider")
		@ParameterizedTest
		void shoulThrowExceptionIfUsernameOrPasswordIsNull(String username, String password) {
			assertThrows(NullPointerException.class, () -> userService.login(username, password));
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
