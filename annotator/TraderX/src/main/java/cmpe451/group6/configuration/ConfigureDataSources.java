package cmpe451.group6.configuration;

import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfigureDataSources {

    // first database
    @Primary
    @Bean(name = "annotatorDb")
    @ConfigurationProperties("app.datasource.annotator")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "dataSourceAnnotatorDb")
    public HikariDataSource firstDataSource(@Qualifier("annotatorDb") DataSourceProperties properties) {
        return (HikariDataSource) properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    // second database
    @Bean(name = "mainDb")
    @ConfigurationProperties("app.datasource.main")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSourceMainDb")
    public HikariDataSource secondDataSource(@Qualifier("mainDb") DataSourceProperties properties) {
        return (HikariDataSource) properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }
}