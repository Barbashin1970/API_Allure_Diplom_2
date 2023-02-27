import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация рандомного пользователя с заполнением всех полей корректно")
    @Description("Позитивная проверка - успешная авторизация")
    public void randomUserShouldSuccessLogin() {
        user = User.createRandomUser(); // Создаем рандомного пользователя
        response = userSteps.createUser(user);
        token = response.then().extract().body().path("accessToken");
        response = userSteps.loginUser(user, token);
        userSteps.deleteUser(token);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация существующего пользователя - профиль тестировщика на сайте")
    @Description("Позитивная проверка личного профиля тестировщика - успешная авторизация")
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
        user = User.createRandomUser(); // Создаем рандомного пользователя
        response = userSteps.createUser(user);
        token = response.then().extract().body().path("accessToken");
        String email = user.getEmail();
        user.setEmail("wrong@mailbox.com");
        String password = user.getPassword();
        user.setPassword("wrongPassword");
        response = userSteps.loginUser(user, token);
        user.setEmail(email);
        user.setPassword(password);
        userSteps.deleteUser(token);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);

    }
}
