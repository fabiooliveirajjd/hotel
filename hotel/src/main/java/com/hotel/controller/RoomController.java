package com.hotel.controller;

import com.hotel.exception.PhotoRetrievalException;
import com.hotel.model.BookedRoom;
import com.hotel.model.Room;
import com.hotel.response.BookingResponse;
import com.hotel.response.RoomResponse;
import com.hotel.service.BookingService;
import com.hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um controlador de quarto
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    private final IRoomService roomService;
    private final BookingService bookingService;

    // Esse método adiciona um novo quarto
    @PostMapping("/add/new-room") // Define a rota do endpoint
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo, // Define o parâmetro da requisição
            @RequestParam("roomType") String roomType, // Define o parâmetro da requisição
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException { // Define o parâmetro da requisição
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice); // Adiciona um novo quarto
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), // Cria uma resposta
                savedRoom.getRoomPrice()); // Cria uma resposta
        return ResponseEntity.ok(response); // Retorna uma resposta
    }

    // Esse método retorna uma lista de tipos de quarto distintos
    @GetMapping("/room/types") // Define a rota do endpoint
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    } // Retorna uma lista de tipos de quarto

    // Esse método retorna uma lista de todos os quartos
    @GetMapping("/all-rooms")// Define a rota do endpoint
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms(); // Obtém todos os quartos
        List<RoomResponse> roomResponses = new ArrayList<>(); // Cria uma lista de respostas
        for (Room room : rooms) { // Criar um loop sobre os quartos
            byte[] photoBytes = roomService.getRoomPhotoByRoomId (room.getId()); // Obtém a foto do quarto
            if (photoBytes != null && photoBytes.length > 0) { // Verifica se a foto não está vazia
                String base64Photo = Base64.encodeBase64String(photoBytes); // Codifica a foto em base64
                RoomResponse roomResponse = getRoomResponse(room); // Cria uma resposta
                roomResponse.setPhoto(base64Photo); // Define a foto da resposta
                roomResponses.add(roomResponse); // Adiciona a resposta à lista
            }
        }
        return ResponseEntity.ok(roomResponses); // Retorna uma resposta
    }

    // Esse método deleta um quarto por id
    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId()); // Obtém todas as reservas por id do quarto
        List<BookingResponse> bookingInfo = bookings // Cria uma lista de informações de reserva
                .stream() // Cria um stream
                .map(booking -> new BookingResponse(booking.getBookingId(), // Mapeia as reservas para informações de reserva
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null; // Inicializa os bytes da foto
        Blob photoBlob = room.getPhoto(); // Obtém a foto do quarto
        if (photoBlob != null) { // Verifica se a foto não está vazia
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length()); // Obtém os bytes da foto
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo"); // Lança uma exceção
            }
        }
        return new RoomResponse(room.getId(),
                room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), photoBytes, bookingInfo);
    }

    // Esse método retorna uma lista de reservas por id do quarto
    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);

    }

}
