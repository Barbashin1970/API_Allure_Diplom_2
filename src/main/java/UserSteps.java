import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Создаём профиль пользователя")
    public Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Configuration.URL)
                .body(user).post(Configuration.REGISTER);
    }

    @Step("Удаляем профиль пользователя")
    public void deleteUser(String token) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(Configuration.URL).delete(Configuration.USER);
    }

    @Step("Вносим изменения в профиль пользователя")
    public Response updateUser(User newUser, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(Configuration.URL)
                .body(newUser).patch(Configuration.USER);
    }

    @Step("Авторизация по одному параметру - пользователь")
    public Response loginUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Configuration.URL)
                .body(user).post(Configuration.LOGIN);
    }

    @Step("Авторизация по двум параметрам - пользователь и токен")
    public Response loginUser(User user, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(Configuration.URL)
                .body(user).post(Configuration.LOGIN);
    }

}