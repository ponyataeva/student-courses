package authentication.service.impl;

import authentication.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource("${users.file}")
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment environment;

    @Override
    public boolean userExists(String user, String password) {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
            return false;
        }
        String userPassword = environment.getProperty(user);
        return StringUtils.isNotEmpty(userPassword) && StringUtils.equals(userPassword, password);
    }
}
