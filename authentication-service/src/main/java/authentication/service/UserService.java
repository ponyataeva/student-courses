package authentication.service;

/**
 * Service for work with User.
 */
public interface UserService {

    /**
     * Check that user with provided name and password exist.
     *
     * @param user name str
     * @param password password str
     * @return result of check
     */
    boolean userExists(String user, String password);
}
