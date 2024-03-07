package com.hotel.service;

import com.hotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/*
    * Essa interface é um serviço que contém métodos para acessar dados de quarto no banco de dados.
 */
public interface IRoomService {

    // Esse método adiciona um novo quarto
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException;

    // Esse método retorna uma lista de tipos de quarto distintos
    List<String> getAllRoomTypes();

    // Esse método retorna uma lista de todos os quartos
    List<Room> getAllRooms();

    // Esse método retorna a foto de um quarto por id
    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;

    // Esse método deleta um quarto por id
    void deleteRoom(Long roomId);

    // Esse método atualiza um quarto
    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);

    // Esse método retorna um quarto por id
    Optional<Room> getRoomById(Long roomId);

    // Esse método retorna uma lista de quartos disponíveis por tipo e datas
    List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
