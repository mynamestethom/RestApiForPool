package com.example.RestApiPool.model;


import java.time.LocalDateTime;

public class Client {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime createdAt;

    public Client(){}

    public Client(Long id, String name, String phone, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }


    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
