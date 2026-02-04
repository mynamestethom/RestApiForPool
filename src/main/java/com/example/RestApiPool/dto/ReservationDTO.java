package com.example.RestApiPool.dto;


public class ReservationDTO {
    private Long clientId;
    private String datetime; // пример данных даты "2024-01-15T10:00"

    public ReservationDTO(Long clientId, String datetime) {
        this.clientId = clientId;
        this.datetime = datetime;
    }

    public ReservationDTO(){}

    public Long getClientId() {
        return clientId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "clientId=" + clientId +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
