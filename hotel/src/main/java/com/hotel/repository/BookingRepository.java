package com.hotel.repository;

import com.hotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interface que contém métodos para acessar dados de reservas no banco de dados
 */
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

    // Esse método retorna uma lista de reservas por id do quarto
    List<BookedRoom> findByRoomId(Long roomId);

    // Esse método retorna uma reserva por código de confirmação
    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    // Esse método retorna uma lista de reservas por email do hóspede
    List<BookedRoom> findByGuestEmail(String email);
}
