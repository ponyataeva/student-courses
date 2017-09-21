package basic.security.config;

import basic.security.config.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static basic.Constants.LOGIN_PATH;
import static basic.Constants.USER_PATTERN;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Add token filter to some path.
     */
    @Bean
    public FilterRegistrationBean tokenFilterRegistrationBean() throws Exception {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TokenFilter securityFilter = new TokenFilter();
        registrationBean.setFilter(securityFilter);
        registrationBean.addUrlPatterns(USER_PATTERN, LOGIN_PATH);

        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }
}
