package data;

import data.dto.UserInfo;
import data.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;


/**
 * Controller for Data Service
 */
@RestController
public class DataController {

    public static final String USER_INFO = "/data/{user-id}";

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public DataController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    /**
     * Get user data by given id.
     *
     * @param userId user
     * @return ResponseEntity :
     * with 403 code in case if basic authentication was not passed.
     * with 404 code in case if there is no data with given user-id.
     */
    @RequestMapping(value = USER_INFO
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getData(@PathVariable("user-id") String userId) throws IOException {
        UserInfo userInfo = userInfoRepository.findOne(new BigInteger(userId));
        if (userInfo != null) {
            return new ResponseEntity(userInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
