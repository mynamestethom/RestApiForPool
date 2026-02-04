package com.example.RestApiPool.service;

import com.example.RestApiPool.dto.ClientDTO;
import com.example.RestApiPool.dto.ClientResponseDTO;
import com.example.RestApiPool.dto.ClientShortResponseDTO;
import com.example.RestApiPool.model.Client;
import com.example.RestApiPool.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<ClientShortResponseDTO> getAllClients(){
        try {
            List<Client> clients = clientRepository.findAll();
            return clients.stream()
                    .map(client -> new ClientShortResponseDTO(client.getId(), client.getName()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка клиентов", e);
        }
    }


    public ClientResponseDTO getClientById(Long id){
        try {
            Client client = clientRepository.findById(id);
            if (client == null) {
                throw new RuntimeException("Клиент с ID " + id + " не найден");
            }
            return new ClientResponseDTO(client.getId(), client.getName(), client.getPhone(), client.getEmail() );
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении клиента", e);
        }
    }

    public ClientResponseDTO createClient(ClientDTO clientDTO){
        try {

            if (!clientRepository.isPhoneUnique(clientDTO.getPhone())) {
                throw new RuntimeException("Клиент с таким телефоном уже существует");
            }

            if (!clientRepository.isEmailUnique(clientDTO.getEmail())) {
                throw new RuntimeException("Клиент с таким email уже существует");
            }

            Client client = new Client();
            client.setName(clientDTO.getName());
            client.setPhone(clientDTO.getPhone());
            client.setEmail(clientDTO.getEmail());

            Long id = clientRepository.create(client);

            return new ClientResponseDTO(id, client.getName(), client.getPhone(), client.getEmail()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании клиента", e);
        }
    }

    public ClientResponseDTO updateClient(Long id, ClientDTO clientDTO){
        try{
            if (!clientRepository.existsById(id)){
                throw new RuntimeException("Клиент с ID " + id + " не найден");
            }

            if (!clientRepository.isPhoneUniqueForUpdate(id, clientDTO.getPhone())) {
                throw new RuntimeException("Телефон уже используется другим клиентом");
            }

            if (!clientRepository.isEmailUniqueForUpdate(id, clientDTO.getEmail())) {
                throw new RuntimeException("Email уже используется другим клиентом");
            }

            Client client = new Client();
            client.setName(clientDTO.getName());
            client.setPhone(clientDTO.getPhone());
            client.setEmail(clientDTO.getEmail());

            int updated = clientRepository.update(id, client);
            if(updated == 0){
                throw new RuntimeException("Не удалось обновить клиента");
            }
            return new ClientResponseDTO(id, client.getName(), client.getPhone(), client.getEmail()
            );
        }catch(SQLException e){
            throw new RuntimeException("Ошибка при обновлении клиента", e);
        }
    }
}
