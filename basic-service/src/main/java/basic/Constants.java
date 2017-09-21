package basic;

public interface Constants {

    String GET_USER_PATH = "/user/{user_id}";
    String USER_PATTERN = "/user/*";
    String LOGIN_PATH = "/login";
    String USER_DATA_PATH = "/data/{user_id}";
    String AUTH_PATH = "/auth";

    String CSRF_TOKEN_ERROR = "CSRF token doesn't matches";
}
