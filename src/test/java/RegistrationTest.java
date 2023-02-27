import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/*
Создание пользователя:
создать уникального пользователя;
создать пользователя, который уже зарегистрирован;
создать пользователя и не заполнить одно из обязательных полей.
*/
public class RegistrationTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание профиля пользователя с рандомными данными")
    @Description("Проверяем что токен не нулевой")
    public void successRegistrationTest() {
        user = User.createRandomUser();
        response = userSteps.createUser(user);
        String token = response.then().extract().body().path("accessToken");
        userSteps.deleteUser(token);
        response.then()
                .body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание двух одинаковых профилей пользователя невозможно")
    @Description("Проверяем что текст ошибки содержит информацию о существовании такого профиля")
    public void existRegistrationTest() {
        user = User.getExistUser();
        response = userSteps.createUser(user);
        response.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверяем обязательность поля пароль при регистрации")
    @Description("Проверяем что без пароля вернется текст ошибки - что поле обязательное")
    public void registrationWithEmptyPasswordTest() {
        user = User.createUserWithEmptyPassword();
        response = userSteps.createUser(user);
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверяем обязательность поля e-mail при регистрации")
    @Description("Проверяем что без e-mail вернется текст ошибки - что поле обязательное")
    public void registrationWithEmptyEmailTest() {
        user = User.createUserWithEmptyEmail();
        response = userSteps.createUser(user);
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверяем обязательность поля имя при регистрации")
    @Description("Проверяем что без имени вернется текст ошибки - что поле обязательное")
    public void registrationWithEmptyNameTest() {
        user = User.createUserWithEmptyName();
        response = userSteps.createUser(user);
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверяем обязательность всех полей при регистрации пользователя")
    @Description("Проверяем что со всеми пустыми полями формы регистрации вернется текст ошибки - что поле обязательное")
    public void registrationWithEmptyUserTest() {
        user = User.createEmptyUser();
        response = userSteps.createUser(user);
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
