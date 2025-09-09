package org.example.trangdangnhap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    // Cấu hình cho Docker PostgreSQL
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/DBUser";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres123";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    private DBConnection() {
    }
}
