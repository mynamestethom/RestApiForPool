package com.example.RestApiPool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String testDatabase() {
        try {
            List<String> tables = jdbcTemplate.queryForList(
                    "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
                    String.class
            );

            StringBuilder result = new StringBuilder();
            result.append("Таблицы в базе данных:<br>");

            for (String table : tables) {
                result.append("<h3>").append(table).append(":</h3>");

                if (table.equals("CLIENTS")) {
                    List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM clients");
                    for (Map<String, Object> row : rows) {
                        result.append("ID: ").append(row.get("ID"))
                                .append(", Имя: ").append(row.get("NAME"))
                                .append(", Телефон: ").append(row.get("PHONE"))
                                .append("<br>");
                    }
                } else if (table.equals("WORK_TIME")) {
                    List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM work_time");
                    for (Map<String, Object> row : rows) {
                        result.append("День недели: ").append(row.get("DAY_OF_WEEK"))
                                .append(", Начало: ").append(row.get("START_TIME"))
                                .append(", Конец: ").append(row.get("END_TIME"))
                                .append("<br>");
                    }
                }
            }

            return result.toString();
        } catch (Exception e) {
            return "Ошибка при подключении к базе данных: " + e.getMessage();
        }
    }
}
