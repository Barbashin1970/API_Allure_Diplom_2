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
Изменение данных пользователя:
- с авторизацией,
- без авторизации,
Для обеих ситуаций нужно проверить, что любое поле можно изменить.
Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
 */
public class ChangeUserDataTest {

    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private String email;
    private String password;
    private String name;
    private String token;
    private User user;

    @Before
    public void setup() {
        user = User.createRandomUser();
        response = userSteps.createUser(user);
        token = response
                .then()
                .extract()
                .body()
                .path("accessToken");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменить пароль без авторизации нельзя")
    @Description("Негативная проверка - ожидаем 401")
    public void updatePasswordShouldBeError() {
        password = user.getPassword();
        user.setPassword(password + "password");
        response = userSteps.updateUser(user, "null");
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поменять имя и e-mail после авторизации возможно")
    @Description("Позитивная проверка - ожидаем 200")
    public void shouldUpdateEmailAndName() {
        email = user.getEmail();
        name = user.getName();
        user.setEmail(email + "email");
        user.setName(name + "name");
        response = userSteps.updateUser(user, token);
        user.setEmail(email);
        user.setName(name);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поменять имя и e-mail без авторизации нельзя")
    @Description("Негативная проверка - ожидаем 401")
    public void updateEmailAndNameShouldBeError() {
        email = user.getEmail();
        name = user.getName();
        user.setEmail(email + "email");
        user.setName(name + "name");
        response = userSteps.updateUser(user, "null");
        user.setEmail(email);
        user.setName(name);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поменять пароль после авторизации возможно")
    @Description("Позитивная проверка - ожидаем 200")
    public void shouldUpdatePassword() {
        password = user.getPassword();
        user.setPassword(password + "password");
        response = userSteps.updateUser(user, token);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @After
    public void teardown() {
        if (token != null) {
            userSteps.deleteUser(token);
        } else {
            throw new RuntimeException("Проверьте - Ваша ручка POST создание аккаунта, возможно, создала токен  == null");
        }
    }
}