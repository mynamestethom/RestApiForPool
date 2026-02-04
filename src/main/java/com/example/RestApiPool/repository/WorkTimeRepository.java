package com.example.RestApiPool.repository;

import com.example.RestApiPool.database.DataBaseUtil;
import com.example.RestApiPool.model.WorkTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

@Repository
public class WorkTimeRepository {

    @Autowired
    private DataBaseUtil databaseUtil;

    public WorkTime getByDayOfWeek(int dayOfWeek) throws SQLException {
        String sql = "SELECT id, day_of_week, start_time, end_time FROM work_time WHERE day_of_week = ?";
        return databaseUtil.querySingle(sql, this::mapWorkTime, dayOfWeek);
    }

    public boolean isWorkingHour(int dayOfWeek, LocalTime time) throws SQLException {
        WorkTime workTime = getByDayOfWeek(dayOfWeek);
        if (workTime == null) return false;

        return !time.isBefore(workTime.getStartTime()) &&
                !time.isAfter(workTime.getEndTime().minusHours(1)); // минус 1 час, так как запись на 1 час
    }

    public LocalTime getStartTime(int dayOfWeek) throws SQLException {
        WorkTime workTime = getByDayOfWeek(dayOfWeek);
        return workTime != null ? workTime.getStartTime() : null;
    }

    public LocalTime getEndTime(int dayOfWeek) throws SQLException {
        WorkTime workTime = getByDayOfWeek(dayOfWeek);
        return workTime != null ? workTime.getEndTime() : null;
    }

    private WorkTime mapWorkTime(ResultSet rs) throws SQLException {
        return new WorkTime(
                rs.getLong("id"),
                rs.getInt("day_of_week"),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime()
        );
    }
}
