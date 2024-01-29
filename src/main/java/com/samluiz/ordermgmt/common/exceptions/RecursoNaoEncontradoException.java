package com.samluiz.ordermgmt.common.exceptions;

import java.util.UUID;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(UUID id) {
        super("O recurso com o id " + id + " não foi encontrado.");
    }
}
