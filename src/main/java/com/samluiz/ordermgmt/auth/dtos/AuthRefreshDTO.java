package com.samluiz.ordermgmt.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthRefreshDTO {

    @JsonProperty("refresh_token")
    private String refreshToken;

    public AuthRefreshDTO() {
    }

    public AuthRefreshDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
