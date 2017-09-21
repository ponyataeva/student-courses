package data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Add class description
 */
@EnableJpaRepositories(basePackages = "data.repositories")
@Configuration
public class DbConfig {

    private static final String HIBERNATE_DIALECT_VAR = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL_VAR = "hibernate.show_sql";
    private static final String HIBERNATE_HBM2DDL_AUTO_VAR = "hibernate.hbm2ddl.auto";

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
        managerDataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        managerDataSource.setUrl(environment.getProperty("jdbc.url"));
        managerDataSource.setUsername(environment.getProperty("jdbc.user"));
        managerDataSource.setPassword(environment.getProperty("jdbc.pass"));
        return managerDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setPackagesToScan("data");
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setJpaProperties(additionalProperties());
        return entityManager;
    }

    private Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        if (environment.getProperty(HIBERNATE_HBM2DDL_AUTO_VAR) != null) {
            hibernateProperties.setProperty(HIBERNATE_HBM2DDL_AUTO_VAR, environment.getProperty(HIBERNATE_HBM2DDL_AUTO_VAR));
        }
        if (environment.getProperty(HIBERNATE_DIALECT_VAR) != null) {
            hibernateProperties.setProperty(HIBERNATE_DIALECT_VAR, environment.getProperty(HIBERNATE_DIALECT_VAR));
        }
        if (environment.getProperty(HIBERNATE_SHOW_SQL_VAR) != null) {
            hibernateProperties.setProperty(HIBERNATE_SHOW_SQL_VAR, environment.getProperty(HIBERNATE_SHOW_SQL_VAR));
        }
        return hibernateProperties;
    }
}
