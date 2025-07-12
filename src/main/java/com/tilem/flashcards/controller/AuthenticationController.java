package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.AuthenticationRequest;
import com.tilem.flashcards.data.dto.AuthenticationResponse;
import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.dto.UserResponseDTO;
import com.tilem.flashcards.mapper.UserMapper;
import com.tilem.flashcards.service.UserService;
import com.tilem.flashcards.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final UserMapper userMapper;

	public AuthenticationController(
			AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, UserMapper userMapper) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.userMapper = userMapper;
	}

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails =
				userService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserDTO userDTO) {
		UserDTO createdUserDTO = userService.create(userDTO);
		return ResponseEntity.ok(userMapper.toResponseDto(userMapper.toEntity(createdUserDTO)));
	}
}
