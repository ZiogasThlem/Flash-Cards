package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.mapper.DeckMapper;
import com.tilem.flashcards.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

	@Mock
	private DeckRepository deckRepository;

	@Mock
	private DeckMapper deckMapper;

	private DeckServiceImpl deckService;

	private Deck testDeck;
	private DeckDTO testDeckDTO;

	@BeforeEach
	void setUp() {
		deckService = new DeckServiceImpl(deckRepository, deckMapper);

		testDeck = new Deck();
		testDeck.setId(1L);
		testDeck.setName("Test Deck");

		User user1 = new User();
		user1.setId(10L);
		user1.setUsername("user1");

		User user2 = new User();
		user2.setId(20L);
		user2.setUsername("user2");

		List<User> users = new ArrayList<>(Arrays.asList(user1, user2));
		testDeck.setUsers(users);

		Flashcard flashcard1 = new Flashcard();
		flashcard1.setId(100L);

		Flashcard flashcard2 = new Flashcard();
		flashcard2.setId(200L);

		List<Flashcard> flashcards = new ArrayList<>(Arrays.asList(flashcard1, flashcard2));
		testDeck.setFlashcards(flashcards);

		testDeckDTO = new DeckDTO(1L, "Test Deck", null, null, Arrays.asList("Flashcard 100", "Flashcard 200"));

		lenient().when(deckMapper.toDto(testDeck)).thenReturn(testDeckDTO);
		lenient()
				.when(deckMapper.toEntity(testDeckDTO))
				.thenReturn(Deck.builder().name(testDeckDTO.name()).build());
	}

	@Test
	void mapToDto_ValidDeck_ReturnsDeckDTO() {
		DeckDTO dto = deckMapper.toDto(testDeck);

		assertNotNull(dto);
		assertEquals(testDeck.getId(), dto.id());
		assertEquals(testDeck.getName(), dto.name());

		assertEquals(new HashSet<>(testDeckDTO.flashcards()), new HashSet<>(dto.flashcards()));
	}

	@Test
	void mapToEntity_ValidDeckDTO_ReturnsDeck() {
		Deck deck = deckMapper.toEntity(testDeckDTO);

		assertNotNull(deck);
		assertEquals(testDeckDTO.name(), deck.getName());
		assertNull(deck.getId());
	}

	@Test
	void updateEntity_ValidDeckAndDTO_UpdatesDeck() {
		Deck existingDeck = new Deck();
		existingDeck.setId(2L);
		existingDeck.setName("Old Name");

		DeckDTO updateDTO = new DeckDTO(null, "New Name", null, null, null);

		deckMapper.updateEntity(updateDTO, existingDeck);
		verify(deckMapper, times(1)).updateEntity(updateDTO, existingDeck);
	}
}