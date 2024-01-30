package com.samluiz.ordermgmt.auth.user.dtos;

import com.samluiz.ordermgmt.auth.user.enums.Role;
import com.samluiz.ordermgmt.auth.user.models.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public class CreateUserDTO {

    @NotEmpty(message = "O username é obrigatório.")
    private String username;

    @NotEmpty(message = "A senha é obrigatória.")
    private String password;

    @NotNull(message = "O campo 'roles' não pode ser nulo.")
    private Set<Role> roles;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static User toEntity(CreateUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRoles(dto.getRoles());
        return user;
    }
}
