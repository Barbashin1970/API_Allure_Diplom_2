import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

/*
Логин пользователя:
логин под существующим пользователем,
логин с неверным логином и паролем.
 */

public class LoginTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String token;

    @Before
    public void setup() {
        user = User.createRandomUser(); // Создаем рандомного пользователя для всех тестов
        response = userSteps.createUser(user);
        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация рандомного пользователя с заполнением всех полей корректно")
    @Description("Позитивная проверка - успешная авторизация")
    public void randomUserShouldSuccessLogin() {
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация существующего пользователя с заполнением всех полей корректно")
    @Description("Позитивная проверка - успешная авторизация")
    public void existUserShouldSuccessLogin() {
        response = userSteps.loginUser(user = User.getExistUser());
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация пользователя с неверными e-mail и паролем - невозможна")
    @Description("Проверка авторизации по неверным данным - возвращается ошибка 401")
    public void loginWithWrongPasswordAndEmail() {
        String email = user.getEmail(); // рандомный пользователь user уже создан на шаге Before
        user.setEmail("wrong@mailbox.com");
        String password = user.getPassword();
        user.setPassword("wrongPassword");
        response = userSteps.loginUser(user, token);
        user.setEmail(email);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @After
    public void teardown() {
        userSteps.deleteUser(token);
    }

}