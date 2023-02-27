import java.util.List;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Order getOrderNotCorrect() {
        return new Order(List.of("1234", "2345"));
    }

    public static Order getOrderEmpty() {
        return new Order(List.of());
    }

    public static Order getOrderCorrect() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    }

    /* - проверка - ингредиенты соответствуют ответу сервера -
         "success": true,
         "name": "Бессмертный флюоресцентный бургер",
         "order": { "number": 2870 }
     */
    public static Order getOrderMarsBurger() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa6e"));
    }
        /*
              "success": true,
              "name": "Люминесцентный био-марсианский бургер",
              "order": { "number": 3384 }
         */

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}