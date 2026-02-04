package com.example.RestApiPool.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {
    private Long id;
    private Long clientId;
    private LocalDate reservationDate;
    private LocalTime registrationTime;
    private Boolean canceled = false;
    private String cancelReason;
    private LocalDateTime createdAt;

    public Reservation() {}

    public Reservation(Long clientId, LocalDate reservationDate, LocalTime registrationTime) {
        this.clientId = clientId;
        this.reservationDate = reservationDate;
        this.registrationTime = registrationTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public LocalTime getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(LocalTime registrationTime) { this.registrationTime = registrationTime; }

    public Boolean getCanceled() { return canceled; }
    public void setCanceled(Boolean canceled) { this.canceled = canceled; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", reservationDate=" + reservationDate +
                ", registrationTime=" + registrationTime +
                ", canceled=" + canceled +
                ", cancelReason='" + cancelReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}