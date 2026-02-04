package com.example.RestApiPool.dto;

public class ClientShortResponseDTO {

    private Long id;
    private String name;

    public ClientShortResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ClientShortResponseDTO(){}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClientShortResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
