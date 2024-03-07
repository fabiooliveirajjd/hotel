package com.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um quarto
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false; // Define se o quarto está reservado

    @Lob // Large Object
    private Blob photo;

    // Um quarto pode ter várias reservas
    @OneToMany(mappedBy="room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    // Adiciona uma reserva ao quarto
    public void addBooking(BookedRoom booking){
        if (bookings == null){  // Se não houver reservas, cria uma lista
            bookings = new ArrayList<>(); // Cria uma lista de reservas
        }
        bookings.add(booking); // Adiciona a reserva à lista
        booking.setRoom(this); // Define o quarto da reserva
        isBooked = true; // Define que o quarto está reservado
        String bookingCode = RandomStringUtils.randomNumeric(10); // Gera um código de reserva
        booking.setBookingConfirmationCode(bookingCode); // Define o código de reserva
    }
}