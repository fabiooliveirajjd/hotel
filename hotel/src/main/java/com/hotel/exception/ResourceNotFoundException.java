package com.hotel.exception;

/*
    * Essa classe representa uma exceção personalizada que será lançada quando um recurso não for encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
