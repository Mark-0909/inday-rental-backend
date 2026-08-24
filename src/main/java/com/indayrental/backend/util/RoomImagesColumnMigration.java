package com.indayrental.backend.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoomImagesColumnMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public RoomImagesColumnMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        String dataType = jdbcTemplate.query(
                """
                SELECT DATA_TYPE
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = 'rooms'
                  AND COLUMN_NAME = 'images'
                """,
                rs -> rs.next() ? rs.getString("DATA_TYPE") : null
        );

        if ("varchar".equalsIgnoreCase(dataType) || "char".equalsIgnoreCase(dataType)) {
            jdbcTemplate.execute("ALTER TABLE rooms MODIFY COLUMN images LONGTEXT NOT NULL");
        }
    }
}
