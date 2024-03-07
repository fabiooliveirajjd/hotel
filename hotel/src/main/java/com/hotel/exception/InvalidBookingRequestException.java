package com.hotel.exception;

/*
    * Essa classe representa uma exceção personalizada que será lançada quando houver um erro ao recuperar uma foto.
 */
public class InvalidBookingRequestException extends RuntimeException {
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
