package com.example.RestApiPool.dto;

public class AvailableSlotDTO {
    private String time; // формат: "10:00"
    private int count;

    public AvailableSlotDTO(String time, int count) {
        this.time = time;
        this.count = count;
    }

    public AvailableSlotDTO() {}

    public String getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AvailableSlotDTO{" +
                "time='" + time + '\'' +
                ", count=" + count +
                '}';
    }
}
