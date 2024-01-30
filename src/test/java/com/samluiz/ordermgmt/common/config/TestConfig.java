package com.samluiz.ordermgmt.common.config;

import com.samluiz.ordermgmt.auth.config.SecurityConfig;
import com.samluiz.ordermgmt.auth.jwt.JwtService;
import com.samluiz.ordermgmt.auth.user.repositories.UserRepository;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Import(SecurityConfig.class)
public class TestConfig {

}
