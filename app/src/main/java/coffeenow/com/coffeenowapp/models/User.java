package coffeenow.com.coffeenowapp.models;

public class User {
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "username";
    public static final String USER_PASSWORD = "password";
    public static final String USER_AUTH_DATA = "data";
    public static final String USER_AUTH_ID = "userId";
    public static final String USER_AUTH_TOKEN = "authToken";


    private String id;
    private String username;
    private String authToken;

    public User() { }

    public User(String id, String name) {
        this.id = id;
        this.username = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
