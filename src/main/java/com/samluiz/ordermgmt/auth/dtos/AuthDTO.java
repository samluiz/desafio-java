package com.samluiz.ordermgmt.auth.dtos;

public class AuthDTO {
    private String username;
    private String password;

    public AuthDTO() {
    }

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static AuthDTO create(String username, String password) {
        return new AuthDTO(username, password);
    }
}
