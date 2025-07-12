package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.mapper.UserMapper;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.UserService;
import com.tilem.flashcards.util.AppException;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDTO, UserRepository>
        implements UserService {

    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;
    private final DeckRepository deckRepository;

    public UserServiceImpl(
            UserRepository userRepo, PasswordEncoder passwordEncoder, UserMapper userMapper, DeckRepository deckRepository) {
        super(userRepo, userMapper);
        this.passwordEncoder = passwordEncoder;
        this.deckRepository = deckRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: " + username);
        try {
            User user =
                    repository
                            .findByUsername(username)
                            .orElseThrow(
                                    () -> new UsernameNotFoundException("User not found with username: " + username));
            log.info("User found with username: " + username);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new UsernameNotFoundException("User password not set for username: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), new ArrayList<>());
        } catch (UsernameNotFoundException e) {
            log.warning("User not found: " + username);
            throw e;
        } catch (Exception e) {
            log.error("Error loading user by username: " + username, e);
            throw e;
        }
    }

    @Override
    public UserDTO create(UserDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return super.create(dto);
    }

    @Override
    public UserDTO findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        return super.findAll();
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return super.update(id, dto);
    }

    @Override
    public void assignDeckToUser(Long userId, Long deckId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new AppException("Deck not found"));

        user.addDeck(deck);

        repository.save(user);
    }
}