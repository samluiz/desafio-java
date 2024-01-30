package com.samluiz.ordermgmt.auth.user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samluiz.ordermgmt.auth.user.dtos.CreateUserDTO;
import com.samluiz.ordermgmt.auth.user.dtos.UserDTO;
import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.common.utils.ControllerUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.samluiz.ordermgmt.utils.ControllerTestUtils.criarUsuario;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "EDITOR", "VIEWER"})
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ControllerUtils<User> controllerUtils;

    @Test
    void me_ReturnsAuthenticatedUser() throws Exception {
        User authenticatedUser = CreateUserDTO.toEntity(criarUsuario());
        authenticatedUser.setId(UUID.randomUUID());
        UserDTO userDTO = UserDTO.fromEntity(authenticatedUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/me")
                        .with(user(authenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userDTO.getUsername()))
                .andDo(print());
    }

    @Test
    void findById_ExistingId_ReturnsUser() throws Exception {
        UUID existingUserId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(existingUserId);
        UserDTO userDTO = UserDTO.fromEntity(existingUser);

        when(userService.findById(existingUserId)).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/busca/id/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userDTO.getUsername()))
                .andDo(print());
    }

    @Test
    void create_ValidUser_ReturnsCreatedUser() throws Exception {
        CreateUserDTO dto = criarUsuario();
        User userToCreate = CreateUserDTO.toEntity(dto);
        userToCreate.setPassword("123");
        when(userService.save(any(User.class))).thenReturn(userToCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    void findByUsername_ExistingUsername_ReturnsUser() throws Exception {
        User existingUser = CreateUserDTO.toEntity(criarUsuario());
        UserDTO userDTO = UserDTO.fromEntity(existingUser);

        when(userService.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/busca/username/{username}", existingUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userDTO.getUsername()))
                .andDo(print());
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() throws Exception {
        UUID nonExistingUserId = UUID.randomUUID();
        when(userService.findById(nonExistingUserId)).thenThrow(RecursoNaoEncontradoException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/busca/id/{id}", nonExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void create_ValidUserDTO_ReturnsCreatedUser() throws Exception {
        CreateUserDTO createUserDTO = criarUsuario();
        User createdUser = CreateUserDTO.toEntity(createUserDTO);
        when(userService.save(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .content(objectMapper.writeValueAsString(createUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(createUserDTO.getUsername()))
                .andDo(print());
    }
}