package com.samluiz.ordermgmt.auth.user.controllers;

import com.samluiz.ordermgmt.auth.user.dtos.CreateUserDTO;
import com.samluiz.ordermgmt.auth.user.dtos.UserDTO;
import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import com.samluiz.ordermgmt.common.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
public class UserController implements UserSwagger {

    private final UserService userService;
    private final ControllerUtils<User> controllerUtils;

    public UserController(UserService userService, ControllerUtils<User> controllerUtils) {
        this.userService = userService;
        this.controllerUtils = controllerUtils;
    }

    @Override
    public ResponseEntity<UserDTO> me(User user) {
        UserDTO userResponse = UserDTO.fromEntity(userService.findByUsername(user.getUsername()));
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<UserDTO> findById(UUID id) {
        UserDTO userResponse = UserDTO.fromEntity(userService.findById(id));
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<UserDTO> findByUsername(String username) {
        UserDTO userResponse = UserDTO.fromEntity(userService.findByUsername(username));
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<UserDTO> create(CreateUserDTO dto) {
        User user = CreateUserDTO.toEntity(dto);
        UserDTO userCriado = UserDTO.fromEntity(userService.save(user));
        URI userCriadoUri = controllerUtils.generateURI(userCriado.getId());
        return ResponseEntity.created(userCriadoUri).body(userCriado);
    }
}
