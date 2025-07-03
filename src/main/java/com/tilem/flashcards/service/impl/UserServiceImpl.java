package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.UserService;
import com.tilem.flashcards.util.LogWrapper;
import java.lang.invoke.MethodHandles;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDTO> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        super(userRepo);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UserDTO mapToDto(User entity) {
        log.info("Mapping User entity to DTO for entity ID: " + entity.getId());
        UserDTO userDTO = UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .build();
        log.info("Successfully mapped User entity to DTO for entity ID: " + entity.getId());
        return userDTO;
    }

    @Override
    protected User mapToEntity(UserDTO dto) {
        log.info("Mapping UserDTO to entity for username: " + dto.getUsername());
        User user = new User();
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        log.info("Successfully mapped UserDTO to entity for username: " + dto.getUsername());
        return user;
    }

    @Override
    protected void updateEntity(User entity, UserDTO dto) {
        log.info("Updating User entity with ID: " + entity.getId());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            log.info("Updated password for user ID: " + entity.getId());
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            entity.setUsername(dto.getUsername());
            log.info("Updated username for user ID: " + entity.getId());
        }
        log.info("Successfully updated User entity with ID: " + entity.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: " + username);
        try {
            User user = ((UserRepository) repository).findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            log.info("User found with username: " + username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        } catch (UsernameNotFoundException e) {
            log.warning("User not found: " + username);
            throw e;
        } catch (Exception e) {
            log.error("Error loading user by username: " + username, e);
            throw e;
        }
    }
}
