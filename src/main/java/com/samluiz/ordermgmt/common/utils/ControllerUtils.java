package com.samluiz.ordermgmt.common.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ControllerUtils <T> {

    public Map<String, Object> generateResponse(Page<T> src) {
        Map<String, Object> response = new HashMap<>();
        response.put("total_items", src.getTotalElements());
        response.put("total_pages", src.getTotalPages());
        response.put("current_page", src.getNumber());
        response.put("results", src.getContent());
        return response;
    }

    public URI generateURI(UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
