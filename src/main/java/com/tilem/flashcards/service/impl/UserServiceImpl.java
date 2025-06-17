package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.dto.UserDTO;
import com.tilem.flashcards.entity.User;
import com.tilem.flashcards.exception.AppException;
import com.tilem.flashcards.mapper.UserMapper;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) throws AppException {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new AppException("User not found with id " + id));
        return userMapper.toDto(user);
    }
}