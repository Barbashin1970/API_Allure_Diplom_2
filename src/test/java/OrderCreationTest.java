import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

/*
Создание заказа:
- с авторизацией,
- без авторизации,
- с ингредиентами,
- без ингредиентов,
- с неверным хешем ингредиентов.
 */
public class OrderCreationTest {
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private Response response;

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создаём заказ с авторизацией пользователя")
    @Description("Заказ по валидному токену пользователя возвращает - 200")
    public void createOrderWithAuth() {
        User user = User.createRandomUser();
        response = userSteps.createUser(user);
        String token = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrder(Order.getOrderCorrect(), token);
        userSteps.deleteUser(token);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа с ингредиентами и без авторизации")
    @Description("Успешное создание заказа с пустым токеном пользователя возможно")
    public void createOrderWithIngredients() {
        response = orderSteps.createOrder(Order.getOrderCorrect(), "");
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа без авторизации проходит успешно")
    @Description("Авторизация для создания заказа не обязательна")
    public void createOrderWithoutAuth() {
        response = orderSteps.createOrder(Order.getOrderCorrect(), "token1234");
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Невозможно создать заказ без ингредиентов")
    @Description("Пустой лист ингредиентов возвращает 400")
    public void createOrderWithoutIngredientsShouldBeError() {
        response = orderSteps.createOrder(Order.getOrderEmpty(), "wrong000token");
        response.then().body("message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создаём заказ с невалидным ID ингредиентов")
    @Description("Должна вернуться ошибка 500")
    public void createOrderWithWrongIngredientHashReturnError() {
        Order order = Order.getOrderWrongHash();
        response = orderSteps.createOrder(order, "token1234");
        response.then().
                statusCode(500);
    }

}