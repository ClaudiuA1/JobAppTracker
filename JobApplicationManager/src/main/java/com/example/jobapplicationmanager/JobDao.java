package com.example.jobapplicationmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class JobDao {

    public static void deleteAll() {
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM jobs");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertJob(TableContent job) {
        String sql = """
            INSERT INTO jobs(companyName, jobTitle, applicationDate, technicalTestDate,
                             interview, result, accountInfo, link)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, job.getCompanyName());
            pstmt.setString(2, job.getJobTitle());
            pstmt.setString(3, job.getApplicationDate() != null ? job.getApplicationDate().toString() : null);
            pstmt.setString(4, job.getTechnicalTestDate() != null ? job.getTechnicalTestDate().toString() : null);
            pstmt.setString(5, job.getInterview());
            pstmt.setString(6, job.getResult());
            pstmt.setString(7, job.getAccountInfo());
            pstmt.setString(8, job.getLink());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<TableContent> getAllJobs() {
        ObservableList<TableContent> jobs = FXCollections.observableArrayList();
        String sql = "SELECT * FROM jobs";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jobs.add(new TableContent(
                        rs.getString("companyName"),
                        rs.getString("jobTitle"),
                        rs.getString("applicationDate") != null ? LocalDate.parse(rs.getString("applicationDate")) : null,
                        rs.getString("technicalTestDate") != null ? LocalDate.parse(rs.getString("technicalTestDate")) : null,
                        rs.getString("interview"),
                        rs.getString("result"),
                        rs.getString("accountInfo"),
                        rs.getString("link")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    public static void saveAll(ObservableList<TableContent> jobs) {
        deleteAll();
        for (TableContent job : jobs) {
            insertJob(job);
        }
    }
}
