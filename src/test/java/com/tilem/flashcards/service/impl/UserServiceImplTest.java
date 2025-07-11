package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
    }

    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

	    UsernameNotFoundException thrown =
			    assertThrows(
					    UsernameNotFoundException.class,
					    () -> {
						    userService.loadUserByUsername("nonexistent");
					    });

        assertTrue(thrown.getMessage().contains("User not found with username: nonexistent"));
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}
