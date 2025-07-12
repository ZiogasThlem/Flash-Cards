package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.mapper.UserMapper;
import com.tilem.flashcards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends GenericController<User, UserDTO> {

	private final UserService userService;
	private final UserMapper userMapper;

	public UserController(UserService userService, UserMapper userMapper) {
		super(userService);
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@Override
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return super.getAll();
    }

	@Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

	@Override
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        return super.create(dto);
    }

	@Override
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        return super.update(id, dto);
    }

	@Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

	@PostMapping("/{userId}/decks/{deckId}")
	public ResponseEntity<Void> assignDeckToUser(@PathVariable Long userId, @PathVariable Long deckId) {
		userService.assignDeckToUser(userId, deckId);
		return ResponseEntity.ok().build();
	}
}