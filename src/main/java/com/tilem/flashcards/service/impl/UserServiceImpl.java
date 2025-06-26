package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return map(userRepo.findById(id).orElseThrow());
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        return map(userRepo.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepo.findById(id).orElseThrow();
        user.setUsername(dto.getUsername());
        return map(userRepo.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    private UserDTO map(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
