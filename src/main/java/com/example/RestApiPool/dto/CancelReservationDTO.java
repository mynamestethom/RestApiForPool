package com.example.RestApiPool.dto;


public class CancelReservationDTO {
    private Long clientId;
    private String reason;

    public CancelReservationDTO(Long clientId, String reason) {
        this.clientId = clientId;
        this.reason = reason;
    }

    public CancelReservationDTO(){}

    public Long getClientId() {
        return clientId;
    }

    public String getReason() {
        return reason;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CancelReservationDTO{" +
                "clientId=" + clientId +
                ", reason='" + reason + '\'' +
                '}';
    }
}
