package com.example.RestApiPool.dto;

import java.util.List;

public class ReservationInfoDTO {
    private String time;
    private int count;
    private List<ClientResponseDTO> clients;

    public ReservationInfoDTO(String time, int count, List<ClientResponseDTO> clients) {
        this.time = time;
        this.count = count;
        this.clients = clients;
    }

    public ReservationInfoDTO() {
    }

    public String getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

    public List<ClientResponseDTO> getClients() {
        return clients;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setClients(List<ClientResponseDTO> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "ReservationInfoDTO{" +
                "time='" + time + '\'' +
                ", count=" + count +
                ", clients=" + clients +
                '}';
    }
}
