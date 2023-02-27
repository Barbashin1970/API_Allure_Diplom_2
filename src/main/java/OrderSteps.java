import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создаём заказ, указывая токен пользователя - авторизация")
    public Response createOrder(Order order, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .baseUri(Configuration.URL)
                .body(order)
                .post(Configuration.ORDERS);
    }

    @Step("Создаём заказ, не указывая токен пользователя - без авторизации")
    public Response createOrderNoAuth(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Configuration.URL)
                .body(order)
                .post(Configuration.ORDERS);
    }


    @Step("Запрашиваем данные о заказах пользователя по его токену")
    public Response getUserOrders(String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .baseUri(Configuration.URL)
                .get(Configuration.ORDERS);
    }

    @Step("Запрашиваем данные об ингредиентах")
    public Response getIng() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Configuration.URL)
                .get(Configuration.INGREDIENTS);
    }
}
