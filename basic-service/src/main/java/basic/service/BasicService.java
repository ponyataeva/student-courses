package basic.service;

import basic.dto.SimpleUser;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Service for interaction with other services.
 */
public interface BasicService {

    /**
     * Execute call to data-service for give info about user.
     *
     * @param userId      - load info of this
     * @return ResponseEntity :
     * with 200 code and usr info as body.
     * with 404 code in case if there is no data found in Data service.
     * with 500 code in case if data.login/data.password properties doesn’t match.
     * with 503 code in case if Data service is unavailable.
     */
    ResponseEntity<String> getUserById(String userId);

    /**
     * Execute call to authentication-service for authenticate given user.
     *
     * @param user which should be authorized.
     * @return ResponseEntity :
     * with 200 code if user found.
     * with 401 code in case if authentication is failed.
     * with 500 code in case if auth.login/auth.password properties doesn’t match.
     * with 503 code in case if Authentication service is unavailable
     */
    ResponseEntity doAuth(SimpleUser user, HttpServletRequest request);
}
