package com.hotel.service;

import com.hotel.exception.InvalidBookingRequestException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.model.BookedRoom;
import com.hotel.model.Room;
import com.hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classe que representa o serviço de reserva
 */
@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    // Esse método retorna uma lista de todas as reservas
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Esse método retorna uma lista de reservas por email do hóspede
    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    // Esse método cancela uma reserva por id
    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    // Esse método retorna uma lista de reservas por id do quarto
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    // Esse método salva uma reserva
    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) { // Define a assinatura do método
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){ // Verifica se a data de saída é antes da data de entrada
            throw new InvalidBookingRequestException("A data de check-in deve ser anterior à data de check-ou"); // Lança uma exceção
        }
        Room room = roomService.getRoomById(roomId).get(); // Obtém o quarto pelo id
        List<BookedRoom> existingBookings = room.getBookings(); // Obtém a lista de reservas do quarto
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings); // Verifica se o quarto está disponível
        if (roomIsAvailable){ // Se o quarto estiver disponível
            room.addBooking(bookingRequest); // Adiciona a reserva ao quarto
            bookingRepository.save(bookingRequest); // Salva a reserva
        }else{
            throw  new InvalidBookingRequestException("Desculpe, este quarto não está disponível nas datas selecionadas;"); // Lança uma exceção
        }
        return bookingRequest.getBookingConfirmationCode(); // Retorna o código de confirmação da reserva
    }

    // Esse método retorna uma reserva por código de confirmação
    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode) // Retorna a reserva pelo código de confirmação
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma reserva encontrada com o código de reserva :"+confirmationCode)); // Lança uma exceção

    }

    // Esse método verifica se o quarto está disponível
    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream() // Retorna um fluxo de reservas existentes
                .noneMatch(existingBooking -> // Verifica se nenhuma reserva existente corresponde à condição
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate()) // Verifica se a data de check-in da reserva existente é igual à data de check-in da reserva
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()) // Verifica se a data de check-out da reserva é anterior à data de check-out da reserva existente
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()) // Verifica se a data de check-in da reserva é posterior à data de check-in da reserva existente
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())) // Verifica se a data de check-in da reserva é anterior à data de check-out da reserva existente
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) // Verifica se a data de check-in da reserva é anterior à data de check-in da reserva existente

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())) // Verifica se a data de check-out da reserva é igual à data de check-out da reserva existente
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) // Verifica se a data de check-in da reserva é anterior à data de check-in da reserva existente

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate())) // Verifica se a data de check-out da reserva é posterior à data de check-out da reserva existente

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) // Verifica se a data de check-in da reserva é igual à data de check-out da reserva existente
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())) // Verifica se a data de check-out da reserva é igual à data de check-in da reserva existente

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) // Verifica se a data de check-in da reserva é igual à data de check-out da reserva existente
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())) // Verifica se a data de check-out da reserva é igual à data de check-in da reserva
                );
    }
}