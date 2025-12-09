package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;
    private String password;

    private String name;
    private String surname;
    private String cardNumber;

    private LocalDate createdDate;

    public Client() {
    }

    public Client(String login, String password,
                  String name, String surname,
                  String cardNumber, LocalDate createdDate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.cardNumber = cardNumber;
        this.createdDate = createdDate;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getCardNumber() { return cardNumber; }

    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public LocalDate getCreatedDate() { return createdDate; }

    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
