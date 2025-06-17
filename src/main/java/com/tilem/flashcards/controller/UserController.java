package com.tilem.flashcards.controller;

import com.tilem.flashcards.dto.UserDTO;
import com.tilem.flashcards.exception.AppException;
import com.tilem.flashcards.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) throws AppException {
        return userService.getUserById(id);
    }
}