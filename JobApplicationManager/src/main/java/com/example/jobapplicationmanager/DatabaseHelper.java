package com.example.jobapplicationmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:jobs.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    // Creează baza de date și tabelul
    public static void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS jobs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    companyName TEXT,
                    jobTitle TEXT,
                    applicationDate TEXT,
                    technicalTestDate TEXT,
                    interview TEXT,
                    result TEXT,
                    accountInfo TEXT,
                    link TEXT
                );
            """;
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
