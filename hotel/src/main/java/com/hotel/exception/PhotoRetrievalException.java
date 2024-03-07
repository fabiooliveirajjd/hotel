package com.hotel.exception;

/*
    * Essa classe representa uma exceção personalizada que será lançada quando houver um erro ao recuperar uma foto.
 */
public class PhotoRetrievalException extends RuntimeException {
    public PhotoRetrievalException(String message) {
        super(message);
    }
}