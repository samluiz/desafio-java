package com.samluiz.ordermgmt.auth.user.services;

import com.samluiz.ordermgmt.auth.user.exceptions.UsernameEmUsoException;
import com.samluiz.ordermgmt.auth.user.exceptions.UsernameNaoEncontradoException;
import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_ExistingUsername_ReturnsUserDetails() {
        String existingUsername = "existingUser";
        User existingUser = new User();
        existingUser.setUsername(existingUsername);

        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        User result = (User) userService.loadUserByUsername(existingUsername);

        assertEquals(existingUsername, result.getUsername());
    }

    @Test
    void loadUserByUsername_NonExistingUsername_ThrowsUsernameNotFoundException() {
        String nonExistingUsername = "nonExistingUser";

        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNaoEncontradoException.class, () -> userService.loadUserByUsername(nonExistingUsername));
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        User userToSave = new User();
        userToSave.setUsername("newUser");
        userToSave.setPassword("password");

        when(userRepository.existsByUsername(userToSave.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userToSave.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        User result = userService.save(userToSave);

        assertNotNull(result);
        assertEquals(userToSave.getUsername(), result.getUsername());
    }

    @Test
    void save_DuplicateUsername_ThrowsUsernameEmUsoException() {
        User userToSave = new User();
        userToSave.setUsername("existingUser");

        when(userRepository.existsByUsername(userToSave.getUsername())).thenReturn(true);

        assertThrows(UsernameEmUsoException.class, () -> userService.save(userToSave));
    }

    @Test
    void findByUsername_ExistingUsername_ReturnsUser() {
        String existingUsername = "existingUser";
        User existingUser = new User();
        existingUser.setUsername(existingUsername);

        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        User result = userService.findByUsername(existingUsername);

        assertEquals(existingUsername, result.getUsername());
    }

    @Test
    void findByUsername_NonExistingUsername_ThrowsUsernameNaoEncontradoException() {
        String nonExistingUsername = "nonExistingUser";

        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNaoEncontradoException.class, () -> userService.findByUsername(nonExistingUsername));
    }

    @Test
    void findById_ExistingId_ReturnsUser() {
        UUID existingUserId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(existingUserId);

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(existingUser));

        User result = userService.findById(existingUserId);

        assertEquals(existingUserId, result.getId());
    }

    @Test
    void findById_NonExistingId_ThrowsUsernameNaoEncontradoException() {
        UUID nonExistingUserId = UUID.randomUUID();

        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        assertThrows(UsernameNaoEncontradoException.class, () -> userService.findById(nonExistingUserId));
    }
}
