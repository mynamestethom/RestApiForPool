package com.example.RestApiPool.repository;

import com.example.RestApiPool.database.DataBaseUtil;
import com.example.RestApiPool.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClientRepository {

    @Autowired
    private DataBaseUtil databaseUtil;

    public List<Client> findAll() throws SQLException {
        String sql = "SELECT id, name FROM clients ORDER BY name";
        return databaseUtil.query(sql, this::mapClientShort);
    }

    public Client findById(Long id) throws SQLException {
        String sql = "SELECT id, name, phone, email, created_at FROM clients WHERE id = ?";
        return databaseUtil.querySingle(sql, this::mapClientFull, id);
    }

    public Long create(Client client) throws SQLException {
        String sql = "INSERT INTO clients (name, phone, email) VALUES (?, ?, ?)";
        return databaseUtil.executeInsert(sql, client.getName(), client.getPhone(), client.getEmail());
    }

    public int update(Long id, Client client) throws SQLException {
        String sql = "UPDATE clients SET name = ?, phone = ?, email = ? WHERE id = ?";
        return databaseUtil.executeUpdate(sql, client.getName(), client.getPhone(), client.getEmail(), id);
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clients WHERE id = ?";
        return databaseUtil.queryCount(sql, id) > 0;
    }

    public boolean isPhoneUnique(String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clients WHERE phone = ?";
        return databaseUtil.queryCount(sql, phone) == 0;
    }

    public boolean isEmailUnique(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ?";
        return databaseUtil.queryCount(sql, email) == 0;
    }

    public boolean isPhoneUniqueForUpdate(Long id, String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clients WHERE phone = ? AND id != ?";
        return databaseUtil.queryCount(sql, phone, id) == 0;
    }

    public boolean isEmailUniqueForUpdate(Long id, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ? AND id != ?";
        return databaseUtil.queryCount(sql, email, id) == 0;
    }

    public List<Client> findByName(String name) throws SQLException {
        String sql = "SELECT id, name, phone, email, created_at FROM clients WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        return databaseUtil.query(sql, this::mapClientFull, "%" + name + "%");
    }

    public List<Client> findAllFull() throws SQLException {
        String sql = "SELECT id, name, phone, email, created_at FROM clients ORDER BY name";
        return databaseUtil.query(sql, this::mapClientFull);
    }

    private Client mapClientShort(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        return client;
    }

    private Client mapClientFull(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        if (rs.getTimestamp("created_at") != null) {
            client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return client;
    }
}
