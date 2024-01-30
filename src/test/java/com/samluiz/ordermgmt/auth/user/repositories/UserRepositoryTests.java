package com.samluiz.ordermgmt.auth.user.repositories;

import com.samluiz.ordermgmt.auth.user.dtos.CreateUserDTO;
import com.samluiz.ordermgmt.auth.user.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static com.samluiz.ordermgmt.utils.ControllerTestUtils.criarUsuario;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private String nonExistingUsername;

    @Test
    void findByUsername_ExistingUsername_ReturnsOptionalOfUser() {
        User existingUser = CreateUserDTO.toEntity(criarUsuario());
        userRepository.save(existingUser);

        Optional<User> result = userRepository.findByUsername(existingUser.getUsername());

        assertTrue(result.isPresent());
        assertEquals(existingUser.getUsername(), result.get().getUsername());
    }

    @Test
    void findByUsername_NonExistingUsername_ReturnsEmptyOptional() {
        nonExistingUsername = "nonExistingUser";

        Optional<User> result = userRepository.findByUsername(nonExistingUsername);

        assertTrue(result.isEmpty());
    }

    @Test
    void existsByUsername_ExistingUsername_ReturnsTrue() {
        User existingUser = CreateUserDTO.toEntity(criarUsuario());
        userRepository.save(existingUser);

        boolean result = userRepository.existsByUsername(existingUser.getUsername());

        assertTrue(result);
    }

    @Test
    void existsByUsername_NonExistingUsername_ReturnsFalse() {
        nonExistingUsername = "nonExistingUser";

        boolean result = userRepository.existsByUsername(nonExistingUsername);

        assertFalse(result);
    }

    @Test
    void save_NoException_ReturnsSavedUser() {
        User userToSave = new User();

        User result = userRepository.save(userToSave);

        assertNotNull(result.getId());
    }

    @Test
    void findById_ExistingId_ReturnsOptionalOfUser() {
        User existingUser = CreateUserDTO.toEntity(criarUsuario());
        userRepository.save(existingUser);

        Optional<User> result = userRepository.findById(existingUser.getId());

        assertTrue(result.isPresent());
        assertEquals(existingUser.getId(), result.get().getId());
    }

    @Test
    void findById_NonExistingId_ReturnsEmptyOptional() {
        UUID nonExistingUserId = UUID.randomUUID();

        Optional<User> result = userRepository.findById(nonExistingUserId);

        assertTrue(result.isEmpty());
    }
}