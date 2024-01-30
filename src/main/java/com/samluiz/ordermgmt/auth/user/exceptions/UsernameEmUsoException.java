package com.samluiz.ordermgmt.auth.user.exceptions;

public class UsernameEmUsoException extends RuntimeException {
    public UsernameEmUsoException(String username) {
        super("O username " + username + " já está em uso.");
    }
}
