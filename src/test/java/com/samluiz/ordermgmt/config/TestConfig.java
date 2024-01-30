package com.samluiz.ordermgmt.config;

import com.samluiz.ordermgmt.auth.config.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@Import(SecurityConfig.class)
public class TestConfig {

}
