package authentication;

import authentication.dto.SimpleUser;
import authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Authentication Service
 */
@RestController
public class AuthenticationController {

    public static final String AUTH = "/auth";

    @Autowired
    private UserService userService;

    /**
     * Find provided user credential in file.
     *
     * @param user which should be authenticated.
     * @return ResponseEntity :
     * with 401 in case if there is no match in users.properties file or password is wrong.
     * with 403 in case if basic authentication was not passed.
     */
    @RequestMapping(value = AUTH
            , method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity auth(@RequestBody SimpleUser user) {
        if (userService.userExists(user.getUser(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
