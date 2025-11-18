package app;

import org.springframework.context.annotation.Configuration;

/**
 * Конфігурація бази даних.
 * Spring Boot автоматично створює DataSource та JdbcTemplate
 * на основі налаштувань з application.properties
 */
@Configuration
public class DatabaseConfig {
    // Spring Boot автоконфігурація створює JdbcTemplate автоматично
    // на основі налаштувань з application.properties
}
