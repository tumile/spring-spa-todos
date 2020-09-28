package com.example.server.user.dto;

import javax.validation.constraints.NotBlank;

public class IDTokenRequest {

    @NotBlank
    private String idToken;

    public IDTokenRequest() {
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
