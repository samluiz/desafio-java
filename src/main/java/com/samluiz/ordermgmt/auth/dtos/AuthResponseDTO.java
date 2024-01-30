package com.samluiz.ordermgmt.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponseDTO {

    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
