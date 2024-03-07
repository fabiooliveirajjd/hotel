package com.hotel.repository;

import com.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/*
    * Essa interface é um repositório de dados que contém métodos para acessar dados de quarto no banco de dados.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Esse método retorna uma lista de tipos de quarto distintos
    @Query("SELECT DISTINCT r.roomType FROM Room r") // Define a consulta JPQL
    List<String> findDistinctRoomTypes();

    // Esse método retorna uma lista de quartos disponíveis por tipo e datas
    @Query(" SELECT r FROM Room r " +
            " WHERE r.roomType LIKE %:roomType% " +
            " AND r.id NOT IN (" +
            "  SELECT br.room.id FROM BookedRoom br " +
            "  WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
            ")") // Define a consulta JPQL

    // Define a assinatura do método
    List<Room> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
