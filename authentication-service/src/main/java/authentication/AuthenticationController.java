package authentication;

import authentication.dto.ErrorEntityType;
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

import static authentication.Constants.ErrorMessage.INCORRECT_CREDENTIAL;
import static authentication.Constants.OperationsPath.AUTH_PATH;

/**
 * Add class description
 */
@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = AUTH_PATH
            , method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity auth(@RequestBody SimpleUser user) {
        if (userService.userExists(user.getUser(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ErrorEntityType error = new ErrorEntityType();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(INCORRECT_CREDENTIAL);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
