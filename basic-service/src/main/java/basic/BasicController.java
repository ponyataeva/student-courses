package basic;

import basic.dto.SimpleUser;
import basic.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for Basic Service
 */
@RestController
public class BasicController {

    public static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";
    private static final String USER_INFO = "/user/{user_id}";

    @Autowired
    protected BasicService basicService;

    private HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();

    /**
     * Adds X-CSRF-TOKEN header to response with generated CSRF token in case of successful
     * token.
     */
    @RequestMapping(value = LOGIN, method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody SimpleUser user, HttpServletRequest request) {
        return basicService.doAuth(user, request);
    }

    /**
     * Closes session in case if CSRF token matches
     */
    @RequestMapping(value = LOGOUT, method = RequestMethod.POST)
    public void logout(HttpServletRequest request) {
        csrfTokenRepository.saveToken(null, request, null);
    }

    /**
     * Returns information from Data service about user.
     */
    @RequestMapping(value = USER_INFO
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser(@PathVariable("user_id") String userId) {
        return basicService.getUserById(userId);
    }
}
