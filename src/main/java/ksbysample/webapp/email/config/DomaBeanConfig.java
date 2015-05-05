package ksbysample.webapp.email.config;

import org.seasar.doma.jdbc.dialect.Dialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomaBeanConfig {

    @Value("${doma.dialect}")
    private String domaDialect;

    @Bean
    public Dialect dialect()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (Dialect)Class.forName(domaDialect).newInstance();
    }

}
