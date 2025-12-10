package org.foodapp.web.dto;

public class LegacyAuthResponse {
    private Long id;
    private String login;
    private String name;
    private String surname;
    private String phoneNumber;

    public LegacyAuthResponse(Long id, String login, String name, String surname, String phoneNumber) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
