package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.mapper.FlashcardMapper;
import com.tilem.flashcards.mapper.PromptMapper;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.FlashcardRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.service.PromptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceImplTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private PromptService promptService;

    @Mock
    private AnswerService answerService;

	@Mock
	private FlashcardMapper flashcardMapper;

	@Mock
	private PromptMapper promptMapper;

    @InjectMocks
    private FlashcardServiceImpl flashcardService;

    private Flashcard testFlashcard;
    private FlashcardDTO testFlashcardDTO;
    private Deck testDeck;
    private Prompt testPrompt;

    @BeforeEach
    void setUp() {
        testDeck = new Deck();
        testDeck.setId(1L);
        testDeck.setName("Test Deck");

        testPrompt = new Prompt();
        testPrompt.setId(10L);
        testPrompt.setPromptBody("Test Prompt");
        testPrompt.setHasSingleAnswer(YesNo.Y);

        Answer testAnswer = new Answer();
        testAnswer.setId(100L);
        testAnswer.setPrompt(testPrompt);
        testAnswer.setAnswerBody("Test Answer");
        testPrompt.setAnswers(List.of(testAnswer));

        testFlashcard = new Flashcard();
        testFlashcard.setId(1L);
        testFlashcard.setDeck(testDeck);
        testFlashcard.setPrompt(testPrompt);

	    testFlashcardDTO = new FlashcardDTO(1L, 1L, new PromptDTO(10L, "Test Prompt", YesNo.Y, Collections.singletonList(new AnswerDTO(100L, 10L, "Test Answer", null))), null);

	    lenient().when(flashcardMapper.toDto(testFlashcard)).thenReturn(testFlashcardDTO);
	    lenient().when(promptMapper.toDto(testPrompt)).thenReturn(testFlashcardDTO.prompt());
    }

    @Test
    void getFlashcardsByDeck_ReturnsFlashcardDTOs() {
        when(flashcardRepository.findByDeckId(1L)).thenReturn(Collections.singletonList(testFlashcard));

        List<FlashcardDTO> result = flashcardService.getFlashcardsByDeck(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
	    assertEquals(testFlashcardDTO.id(), result.get(0).id());
        verify(flashcardRepository, times(1)).findByDeckId(1L);
	    verify(flashcardMapper, times(1)).toDto(testFlashcard);
    }
}