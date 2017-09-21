package authentication.service.impl;

import authentication.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@PropertySource("${users.file}")
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment environment;

    @Override
    public boolean userExists(String user, String password) {
        if (isEmpty(user) || isEmpty(password)) {
            return false;
        }
        String userPassword = environment.getProperty(user);
        return isNotEmpty(userPassword) && StringUtils.equals(userPassword, password);
    }
}
