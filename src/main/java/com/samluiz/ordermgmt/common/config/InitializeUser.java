package com.samluiz.ordermgmt.common.config;

import com.samluiz.ordermgmt.auth.user.enums.Role;
import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Configuration
@Transactional
public class InitializeUser implements ApplicationRunner {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(InitializeUser.class);

    public InitializeUser(EntityManager entityManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${admin.credentials.username:null}")
    private String ADMIN_USERNAME;

    @Value("${admin.credentials.password:null}")
    private String ADMIN_PASSWORD;

    public void initializeUser() {
        Optional<User> user = userRepository.findByUsername(ADMIN_USERNAME);

        if (user.isEmpty()) {
            if (!StringUtils.isEmpty(ADMIN_PASSWORD)) {
                log.info("Criando usuário ADMIN");
                User admin = new User();
                admin.setUsername(ADMIN_USERNAME);
                admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_EDITOR, Role.ROLE_VIEWER));

                try {
                    entityManager.persist(admin);
                    log.info("Usuário ADMIN criado com sucesso");
                } catch (Exception e) {
                    log.error("Erro ao criar usuário ADMIN", e);
                }

            } else {
                log.info("Credenciais não foram especificadas.");
            }
        } else {
            log.info("Usuário ADMIN já existe");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeUser();
    }
}
