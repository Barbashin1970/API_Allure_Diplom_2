import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String name;
    private String email;
    private String password;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getExistUser() {
        return new User("user777@user.user", "user1234", "user777user");
    }

    public static User createRandomUser() {
        return new User(RandomStringUtils.randomAlphanumeric(10) + "@burger.ru", "User1234", "UserName");
    }

    public static User createUserWithEmptyPassword() {
        return new User(RandomStringUtils.randomAlphanumeric(10) + "@burger.ru", null, "UserName");
    }

    public static User createUserWithEmptyEmail() {
        return new User(null, "User1234", RandomStringUtils.randomAlphabetic(10));
    }

    public static User createUserWithEmptyName() {
        return new User(RandomStringUtils.randomAlphanumeric(10) + "@burger.ru", "User1234", null);
    }

    public static User createEmptyUser() {
        return new User(null, null, null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}