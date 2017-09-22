package basic.app.config;

import basic.security.config.filter.TokenFilter;
import basic.service.RestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

/**
 * Application config with bean definitions.
 */
@Configuration
public class AppConfig {

    @Bean(name = "tokenFilter")
    public TokenFilter tokenFilterBean() {
        return new TokenFilter();
    }

    @Bean(name = "csrfTokenRepository")
    public HttpSessionCsrfTokenRepository csrfTokenRepositoryBean() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public RestTemplateFactory restTemplateFactoryBean() {
        return new RestTemplateFactory();
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateFactoryBean().getObject();
    }
}
