package com.example.RestApiPool.repository;

import com.example.RestApiPool.database.DataBaseUtil;
import com.example.RestApiPool.dto.ClientResponseDTO;
import com.example.RestApiPool.dto.ReservationInfoDTO;
import com.example.RestApiPool.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationRepository {

    private static final int MAX_CAPACITY = 10;

    @Autowired
    private DataBaseUtil databaseUtil;

    public Long create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (client_id, registration_date, registration_time) " +
                "VALUES (?, ?, ?)";
        return databaseUtil.executeInsert(
                sql,
                reservation.getClientId(),
                reservation.getReservationDate(),
                reservation.getRegistrationTime()
        );
    }

    public boolean cancel(Long reservationId, Long clientId, String reason) throws SQLException {
        String sql = "UPDATE reservations SET canceled = TRUE, cancel_reason = ? " +
                "WHERE id = ? AND client_id = ? AND canceled = FALSE";
        int updated = databaseUtil.executeUpdate(sql, reason, reservationId, clientId);
        return updated > 0;
    }

    public Reservation findById(Long id) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        return databaseUtil.querySingle(sql, this::mapReservation, id);
    }

    public boolean hasReservationOnDate(Long clientId, LocalDate date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE client_id = ? AND registration_date = ? AND canceled = FALSE";
        return databaseUtil.queryCount(sql, clientId, date) > 0;
    }

    public int countByDateTime(LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE registration_date = ? AND registration_time = ? AND canceled = FALSE";
        return databaseUtil.queryCount(sql, date, time);
    }

    public List<Map<String, Object>> getAvailableSlots(LocalDate date) throws SQLException {
        List<Map<String, Object>> availableSlots = new ArrayList<>();

        String sql = "SELECT registration_time, COUNT(*) as count FROM reservations " +
                "WHERE registration_date = ? AND canceled = FALSE " +
                "GROUP BY registration_time";

        Map<LocalTime, Integer> reservedCounts = new HashMap<>();
        databaseUtil.query(sql, rs -> {
            reservedCounts.put(
                    rs.getTime("registration_time").toLocalTime(),
                    rs.getInt("count")
            );
            return null;
        }, date);

        int dayOfWeek = date.getDayOfWeek().getValue();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(22, 0);


        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            //доп проверка что время час
            if (currentTime.getMinute() == 0 && currentTime.getSecond() == 0) {
                int reserved = reservedCounts.getOrDefault(currentTime, 0);
                int available = MAX_CAPACITY - reserved;

                if (available > 0) {
                    Map<String, Object> slot = new HashMap<>();
                    slot.put("time", currentTime.toString());
                    slot.put("count", available);
                    availableSlots.add(slot);
                }
            }
            currentTime = currentTime.plusHours(1);
        }

        return availableSlots;
    }

    public List<ReservationInfoDTO> getReservationsInfo(LocalDate date) throws SQLException {
        String sql = "SELECT r.registration_time, c.id as client_id, c.name, c.phone, c.email " +
                "FROM reservations r " +
                "JOIN clients c ON r.client_id = c.id " +
                "WHERE r.registration_date = ? AND r.canceled = FALSE " +
                "ORDER BY r.registration_time";

        Map<String, ReservationInfoDTO> timeSlotMap = new HashMap<>();

        databaseUtil.query(sql, rs -> {
            String time = rs.getTime("registration_time").toLocalTime().toString();

            ReservationInfoDTO info = timeSlotMap.get(time);
            if (info == null) {
                info = new ReservationInfoDTO(time, 0, new ArrayList<>());
                timeSlotMap.put(time, info);
            }

            ClientResponseDTO client = new ClientResponseDTO(
                    rs.getLong("client_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
            );

            info.getClients().add(client);
            info.setCount(info.getCount() + 1);

            return null;
        }, date);

        return new ArrayList<>(timeSlotMap.values());
    }

    public boolean exists(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE id = ?";
        return databaseUtil.queryCount(sql, id) > 0;
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setClientId(rs.getLong("client_id"));
        reservation.setReservationDate(rs.getDate("registration_date").toLocalDate());
        reservation.setRegistrationTime(rs.getTime("registration_time").toLocalTime());
        reservation.setCanceled(rs.getBoolean("canceled"));
        reservation.setCancelReason(rs.getString("cancel_reason"));

        if (rs.getTimestamp("created_at") != null) {
            reservation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return reservation;
    }

    public List<Reservation> findByClientName(String name) throws SQLException {
        String sql = "SELECT r.* FROM reservations r " +
                "JOIN clients c ON r.client_id = c.id " +
                "WHERE LOWER(c.name) LIKE LOWER(?) AND r.canceled = FALSE " +
                "ORDER BY r.registration_date, r.registration_time";
        return databaseUtil.query(sql, this::mapReservation, "%" + name + "%");
    }
}