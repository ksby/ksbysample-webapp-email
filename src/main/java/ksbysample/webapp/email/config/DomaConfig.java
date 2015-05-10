package ksbysample.webapp.email.config;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DomaConfig implements Config {

    private DataSource dataSource;

    @Autowired
    private Dialect dialect;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public Dialect getDialect() {
        return this.dialect;
    }

    @Configuration
    protected static class DomaBeanConfig {

        @Value("${doma.dialect}")
        private String domaDialect;

        @Bean
        public Dialect dialect()
                throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            return (Dialect)Class.forName(domaDialect).newInstance();
        }

    }

}
