package com.spring.boot.demo.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfiguration {
    @Value("${spring.datasource.url}")
    private String h2DatabaseUrl;
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "h2DataSource")
    public DataSource h2DataSource() {
        log.info("h2DataSource initializing");
        HikariConfig config = new HikariConfig();
        HikariDataSource dataSource;
        config.setJdbcUrl(h2DatabaseUrl);
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
