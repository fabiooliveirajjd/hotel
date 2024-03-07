package com.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Classe que representa a entidade BookedRoom
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long bookingId; // Identificador da reserva

    @Column(name = "check_in")
    private LocalDate checkInDate; // Data de entrada

    @Column(name = "check_out")
    private LocalDate checkOutDate; // Data de saída

    @Column(name = "guest_fullName")
    private String guestFullName; // Nome do hóspede

    @Column(name = "guest_email")
    private String guestEmail; // Email do hóspede

    @Column(name = "adults")
    private int NumOfAdults; // Número de adultos

    @Column(name = "children")
    private int NumOfChildren; // Número de crianças

    @Column(name = "total_guest")
    private int totalNumOfGuest; // Número total de hóspedes

    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode; // Código de confirmação da reserva

    // Mapeamento de muitos para um (muitas reservas para um quarto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room; // Quarto reservado

    // Calcula o número total de hóspedes
    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest = this.NumOfAdults + NumOfChildren;
    }

    // Define o quarto reservado
    public void setNumOfAdults(int numOfAdults) {
        NumOfAdults = numOfAdults; // Define o número de adultos
        calculateTotalNumberOfGuest(); // Calcula o número total de hóspedes
    }

    // Define o número de crianças
    public void setNumOfChildren(int numOfChildren) {
        NumOfChildren = numOfChildren; // Define o número de crianças
        calculateTotalNumberOfGuest(); // Calcula o número total de hóspedes
    }

    // Define o quarto reservado
    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}