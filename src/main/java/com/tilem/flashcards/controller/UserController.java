package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends GenericController<User, UserDTO> {

    public UserController(UserService userService) {
        super(userService);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        return super.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        return super.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    protected UserDTO mapToDto(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .build();
    }
}
