package authentication.service;

public interface UserService {

    boolean userExists(String user, String password);
}
