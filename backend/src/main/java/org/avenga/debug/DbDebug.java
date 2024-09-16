package org.avenga.debug;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

public class DbDebug {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @PostConstruct
    public void logDatabaseUrl() {
        System.out.println("Database URL: " + datasourceUrl);

        System.out.println("Database Username: " + databaseUsername);
    }
}
