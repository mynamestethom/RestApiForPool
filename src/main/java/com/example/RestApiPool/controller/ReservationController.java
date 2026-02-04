package com.example.RestApiPool.controller;

import com.example.RestApiPool.dto.AvailableSlotDTO;
import com.example.RestApiPool.dto.CancelReservationDTO;
import com.example.RestApiPool.dto.ReservationDTO;
import com.example.RestApiPool.dto.ReservationInfoDTO;
import com.example.RestApiPool.model.Reservation;
import com.example.RestApiPool.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/pool")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/time-slots/available")
    public ResponseEntity<?> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<AvailableSlotDTO> slots = reservationService.getAvailableSlots(date);
            return ResponseEntity.ok(slots);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<ReservationInfoDTO> reservations = reservationService.getReservationsInfo(date);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            Long reservationId = reservationService.createReservation(reservationDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("id", reservationId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    @PostMapping("/reservations/{id}/cancel")
    public ResponseEntity<?> cancelReservation(
            @PathVariable Long id,
            @Valid @RequestBody CancelReservationDTO cancelDTO) {
        try {
            reservationService.cancelReservation(id, cancelDTO.getClientId(), cancelDTO.getReason());
            return ResponseEntity.ok(Map.of("message", "Запись успешно отменена"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    //Поиск записей по ФИО Необязательное пожелание
    @GetMapping("/reservations/search")
    public ResponseEntity<?> searchReservationsByName(@RequestParam String name) {
        try {
            List<Reservation> reservations = reservationService.findReservationsByClientName(name);
            return ResponseEntity.ok(reservations);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }
}
