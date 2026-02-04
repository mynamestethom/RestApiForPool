package com.example.RestApiPool.database;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseUtil {

    private final DataSource dataSource;

    public DataBaseUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void execute(String sql) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        }
    }

    public Long executeInsert(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                return null;
            }
        }
    }

    public <T> List<T> query(String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        }

        return results;
    }

    public <T> T querySingle(String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        List<T> results = query(sql, mapper, params);
        return results.isEmpty() ? null : results.get(0);
    }

    public int queryCount(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    public boolean exists(String sql, Object... params) throws SQLException {
        return queryCount(sql, params) > 0;
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    public List<String> getAllTables() throws SQLException {
        return query(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
                rs -> rs.getString("TABLE_NAME")
        );
    }

    public boolean tableExists(String tableName) throws SQLException {
        return exists(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME = ?",
                tableName.toUpperCase()
        );
    }
}
