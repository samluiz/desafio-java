package com.samluiz.ordermgmt.auth.user.dtos;

import com.samluiz.ordermgmt.auth.user.enums.Role;
import com.samluiz.ordermgmt.auth.user.models.User;

import java.util.Set;
import java.util.UUID;

public class UserDTO {

    private UUID id;
    private String username;
    private Set<Role> roles;

    public UserDTO() {
    }

    public UserDTO(UUID id, String username, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRoles());
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
