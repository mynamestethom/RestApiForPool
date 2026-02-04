package com.example.RestApiPool.model;

import java.time.LocalTime;

public class WorkTime {
    private Long id;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkTime(){}

    public WorkTime(Long id, Integer dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    public void setId(Long id) { this.id = id; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return "WorkTime{" +
                "id=" + id +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
