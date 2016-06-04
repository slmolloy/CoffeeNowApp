package coffeenow.com.coffeenowapp.models;

public class Authentication {

    public static final String AUTH_USERID = "userId";
    public static final String AUTH_TOKEN = "authToken";

    private String userId;
    private String authToken;

    public Authentication(String userId, String authToken) {
        this.userId = userId;
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
