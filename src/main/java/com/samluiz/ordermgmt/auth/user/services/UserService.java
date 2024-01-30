package com.samluiz.ordermgmt.auth.user.services;

import com.samluiz.ordermgmt.auth.user.exceptions.UsernameEmUsoException;
import com.samluiz.ordermgmt.auth.user.exceptions.UsernameNaoEncontradoException;
import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNaoEncontradoException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNaoEncontradoException(username));
    }

    public User save(User user) {
        if (!Boolean.TRUE.equals(userRepository.existsByUsername(user.getUsername()))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new UsernameEmUsoException(user.getUsername());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNaoEncontradoException(username));
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UsernameNaoEncontradoException(id.toString()));
    }
}
