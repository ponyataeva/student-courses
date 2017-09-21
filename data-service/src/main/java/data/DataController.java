package data;

import data.dto.ErrorEntityType;
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

import static data.Constants.USER_DATA_PATH;
import static data.Constants.USER_NOT_FOUND_MSG;


/**
 * Add class description
 */
@RestController
public class DataController {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public DataController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @RequestMapping(value = USER_DATA_PATH
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getData(@PathVariable("user-id") String userId) throws IOException {
        UserInfo userInfo = userInfoRepository.findOne(new BigInteger(userId));
        if (userInfo != null) {
            return new ResponseEntity(userInfo, HttpStatus.OK);
        } else {
            ErrorEntityType error = new ErrorEntityType();
            error.setMessage(USER_NOT_FOUND_MSG);
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }
}
