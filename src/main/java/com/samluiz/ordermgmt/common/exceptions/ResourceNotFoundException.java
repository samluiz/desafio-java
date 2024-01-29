package com.samluiz.ordermgmt.common.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(UUID id) {
        super("O recurso com o id " + id + " não foi encontrado.");
    }
}
