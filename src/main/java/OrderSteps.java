import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создаём заказ, указывая токен пользователя")
    public Response createOrder(Order order, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
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
}