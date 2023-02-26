import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersFromUserTest {
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private Response response;
    private Order order;

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получаем список заказов авторизованного пользователя")
    @Description("Создаем два заказа и запрашиваем какие есть заказы у пользователя")
    public void shouldGetOrdersWithAuthUser() {
        User user = User.createRandomUser();
        response = userSteps.createUser(user);
        String token = response.then().extract().body().path("accessToken");
        order = Order.getOrderCorrect();
        response = orderSteps.createOrder(order, token);
        response = orderSteps.createOrder(order, token);
        response = orderSteps.getUserOrders(token);
        userSteps.deleteUser(token);
        response.then()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получаем список заказов неавторизованного пользователя")
    @Description("Создаём заказ без авторизации и должны получить код 401 и сообщение о необходимости авторизации")
    public void getOrdersWithUnauthorizedShouldBeError() {
        order = Order.getOrderCorrect();
        response = orderSteps.createOrder(order, "token1234");
        response = orderSteps.getUserOrders("token1234");
        response.then()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}