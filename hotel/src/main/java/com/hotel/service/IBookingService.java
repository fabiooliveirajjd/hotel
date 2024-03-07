package com.hotel.service;

import com.hotel.model.BookedRoom;

import java.util.List;

/*
    * Essa interface é um serviço que contém métodos para acessar dados de reserva no banco de dados.
 */
public interface IBookingService {

    // Esse método cancela uma reserva por id
    void cancelBooking(Long bookingId);

    // Esse método retorna uma lista de reservas por id de quarto
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    // Esse método salva uma reserva
    String saveBooking(Long roomId, BookedRoom bookingRequest);

    // Esse método retorna uma reserva por código de confirmação
    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    // Esse método retorna uma lista de todas as reservas
    List<BookedRoom> getAllBookings();

    // Esse método retorna uma lista de reservas por email de usuário
    List<BookedRoom> getBookingsByUserEmail(String email);
}