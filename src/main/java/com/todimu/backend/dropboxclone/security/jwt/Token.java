package com.todimu.backend.dropboxclone.security.jwt;

import lombok.Getter;

@Getter
public class Token {

    private final String token;

    public Token(String token) {
        this.token = token;
    }
}
