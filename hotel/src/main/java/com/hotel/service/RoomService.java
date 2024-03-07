package com.hotel.service;

import com.hotel.exception.ResourceNotFoundException;
import com.hotel.model.Room;
import com.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/*
    * Essa classe é um serviço que contém métodos para acessar dados de quarto no banco de dados.
 */
@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;

    // Esse método adiciona um novo quarto
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room(); // Cria um novo objeto Room
        room.setRoomType(roomType); // Define o tipo de quarto
        room.setRoomPrice(roomPrice); // Define o preço do quarto
        if (!file.isEmpty()) { // Verifica se o arquivo não está vazio
            byte[] photoBytes = file.getBytes(); // Obtém os bytes da foto
            Blob photoBlob = new SerialBlob(photoBytes); // Cria um objeto Blob
            room.setPhoto(photoBlob); // Define a foto do quarto
        }
        return roomRepository.save(room); // Salva o quarto no banco de dados
    }

    // Esse método retorna uma lista de tipos de quarto distintos
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    // Esse método retorna uma lista de todos os quartos
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Esse método retorna a foto de um quarto por id
    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(roomId); // Obtém um quarto por id
        if(theRoom.isEmpty()){ // Verifica se o quarto não está presente
            throw new ResourceNotFoundException("Desculpe, não encontramos um quarto com o id: " + roomId); // Lança uma exceção
        }
        Blob photoBlob = theRoom.get().getPhoto(); // Obtém a foto do quarto
        if(photoBlob != null){ // Verifica se a foto não está vazia
            return photoBlob.getBytes(1, (int) photoBlob.length()); // Retorna os bytes da foto
        }
        return null;
    }

    // Esse método deleta um quarto por id
    @Override
    public void deleteRoom(Long roomId) {

    }

    // Esse método atualiza um quarto
    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        return null;
    }

    // Esse método retorna um quarto por id
    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.empty();
    }

    // Esse método retorna uma lista de quartos disponíveis por tipo e datas
    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return null;
    }


}
