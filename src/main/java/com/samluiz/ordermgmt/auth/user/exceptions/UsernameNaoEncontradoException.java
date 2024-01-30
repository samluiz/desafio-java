package com.samluiz.ordermgmt.auth.user.exceptions;

public class UsernameNaoEncontradoException extends RuntimeException {
    public UsernameNaoEncontradoException(String username) {
        super("O username " + username + " não foi encontrado.");
    }
}
