package com.example.RestApiPool.service;

import com.example.RestApiPool.dto.AvailableSlotDTO;
import com.example.RestApiPool.dto.ReservationDTO;
import com.example.RestApiPool.dto.ReservationInfoDTO;
import com.example.RestApiPool.model.Reservation;
import com.example.RestApiPool.repository.ClientRepository;
import com.example.RestApiPool.repository.ReservationRepository;
import com.example.RestApiPool.repository.WorkTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Autowired
    private ClientRepository clientRepository;

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public List<AvailableSlotDTO> getAvailableSlots(LocalDate date){
        try {
            List<Map<String, Object>> slots = reservationRepository.getAvailableSlots(date);

            int dayOfWeek = date.getDayOfWeek().getValue();
            LocalTime startTime = workTimeRepository.getStartTime(dayOfWeek);
            LocalTime endTime = workTimeRepository.getEndTime(dayOfWeek);

            //доп проверка на рабочий день, если поля пустые
            if (startTime == null || endTime == null) {
                throw new RuntimeException("Бассейн не работает в этот день");
            }

            List<AvailableSlotDTO> result = new ArrayList<>();

            for (int i = 0; i < slots.size(); i++) {
                Map<String, Object> slot = slots.get(i);
                LocalTime slotTime = LocalTime.parse((String) slot.get("time"));

                if (workTimeRepository.isWorkingHour(dayOfWeek, slotTime)) {
                    String time = (String) slot.get("time");
                    Integer count = (Integer) slot.get("count");
                    result.add(new AvailableSlotDTO(time, count));
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении доступных слотов", e);
        }
    }

    public List<ReservationInfoDTO> getReservationsInfo(LocalDate date){
        try {
            return reservationRepository.getReservationsInfo(date);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении информации о записях", e);
        }
    }

    public Long createReservation(ReservationDTO reservationDTO){
        try {
            LocalDateTime datetime;
            try {
                datetime = LocalDateTime.parse(reservationDTO.getDatetime(), DATETIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Неверный формат даты. Используйте формат: yyyy-MM-dd'T'HH:mm");
            }

            LocalDate date = datetime.toLocalDate();
            LocalTime time = datetime.toLocalTime();

            if(time.getMinute() != 0 || time.getSecond() != 0) {
                throw new RuntimeException("Запись возможна только на целый час (например, 10:00, 11:00)");
            }

            if(!clientRepository.existsById(reservationDTO.getClientId())){
                throw new RuntimeException("Клиент не найден");
            }

            int dayOfWeek = date.getDayOfWeek().getValue();
            if(!workTimeRepository.isWorkingHour(dayOfWeek, time)){
                throw new RuntimeException("Время вне рабочего дня бассейна");
            }

            if(reservationRepository.hasReservationOnDate(reservationDTO.getClientId(), date)){
                throw new RuntimeException("Клиент уже записан на этот день");
            }

            int currentCount = reservationRepository.countByDateTime(date, time);
            //запись в день меньше 10
            if (currentCount >= 10){
                throw new RuntimeException("На это время все места заняты");
            }

            Reservation reservation = new Reservation();
            reservation.setClientId(reservationDTO.getClientId());
            reservation.setReservationDate(date);
            reservation.setRegistrationTime(time);

            return reservationRepository.create(reservation);


        } catch (SQLException e){
            throw new RuntimeException("Ошибка при создании записи", e);
        }
    }

    public void cancelReservation(Long reservationId, Long clientId, String reason){
        try{

            Reservation reservation = reservationRepository.findById(reservationId);
            if(reservation == null) {
                throw new RuntimeException("Запись не найдена");
            }

            if(!reservation.getClientId().equals(clientId)){
                throw new RuntimeException("Запись не принадлежит указанному клиенту");
            }

            if(reservation.getCanceled()){
                throw new RuntimeException("Запись уже отменена");
            }

            boolean success = reservationRepository.cancel(reservationId, clientId, reason);
            if (!success) {
                throw new RuntimeException("Не удалось отменить запись");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при отмене записи", e);
        }
    }


    public List<Reservation> findReservationsByClientName(String name){
        try {
            return reservationRepository.findByClientName(name);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске записей", e);
        }
    }
}
