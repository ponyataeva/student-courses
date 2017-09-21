package authentication;

/**
 * Add class description
 */
public interface Constants {


    interface OperationsPath {
        String AUTH_PATH = "/auth";
        String USERS_PROPERTIES_VAR = "${users.file}";
    }

    interface ErrorMessage {
        String USER_NOT_FOUND_MSG = "There is no data with given user-id";
        String INCORRECT_CREDENTIAL = "user/password pair was not found";
    }
}
