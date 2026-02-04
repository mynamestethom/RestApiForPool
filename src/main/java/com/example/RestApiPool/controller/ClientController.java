package com.example.RestApiPool.controller;

import com.example.RestApiPool.dto.ClientDTO;
import com.example.RestApiPool.dto.ClientResponseDTO;
import com.example.RestApiPool.dto.ClientShortResponseDTO;
import com.example.RestApiPool.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/pool")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public ResponseEntity<List<ClientShortResponseDTO>> getAllClients() {
        try {
            List<ClientShortResponseDTO> clients = clientService.getAllClients();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        try {
            ClientResponseDTO client = clientService.getClientById(id);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сервера");
        }
    }

    @PostMapping("/clients")
    public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO) {
        try {
            ClientResponseDTO createdClient = clientService.createClient(clientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сервера");
        }
    }

    @PatchMapping("/clients/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        try {
            ClientResponseDTO updatedClient = clientService.updateClient(id, clientDTO);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сервера");
        }
    }
}