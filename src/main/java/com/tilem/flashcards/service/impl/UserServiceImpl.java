package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.UserService;
import com.tilem.flashcards.util.EncryptionUtil;
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
        return userRepo.findAll().stream()
                .map(this::mapWithoutPassword)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return mapWithoutPassword(userRepo.findById(id).orElseThrow());
    }

    @Override
    public UserDTO createUser(UserDTO dto) throws Exception {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(EncryptionUtil.encrypt(dto.getPassword()));
        return mapWithoutPassword(userRepo.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) throws Exception {
        User user = userRepo.findById(id).orElseThrow();
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(EncryptionUtil.encrypt(dto.getPassword()));
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(dto.getUsername());
        }
        return mapWithoutPassword(userRepo.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public String getDecryptedPassword(Long userId) throws Exception {
        User user = userRepo.findById(userId).orElseThrow();
        return EncryptionUtil.decrypt(user.getPassword());
    }

    private UserDTO mapWithoutPassword(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
