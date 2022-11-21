package com.rotar.PhotoEditorWeb.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class LocalDataSource {

    private final String URL = "jdbc:postgresql://localhost:5432/photoApp";
    private final String USER = "root";
    private final String DRIVER = "org.postgresql.Driver";
    private final String PASSWORD = "mary1995";

    @Bean
    public DataSource getSingletonDatasource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(URL);
        driverManagerDataSource.setUsername(USER);
        driverManagerDataSource.setPassword(PASSWORD);
        driverManagerDataSource.setDriverClassName(DRIVER);
        //driverManagerDataSource.
        System.out.println("-----CONNECTION SUCCESSFUL!----");
        return driverManagerDataSource;
    }
}