package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.BlobDataDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.BlobData;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.repository.BlobDataRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private BlobDataRepository blobDataRepository;

    @InjectMocks
    private FlashcardServiceImpl flashcardService;

    private Flashcard testFlashcard;
    private FlashcardDTO testFlashcardDTO;
    private Deck testDeck;
    private Prompt testPrompt;
    private BlobData testBlobData;

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

        testBlobData = new BlobData();
        testBlobData.setId(1000L);
        testBlobData.setData(new byte[]{1, 2, 3});
        testBlobData.setMimeType("image/png");

        testFlashcard = new Flashcard();
        testFlashcard.setId(1L);
        testFlashcard.setDeck(testDeck);
        testFlashcard.setPrompt(testPrompt);
        testFlashcard.setHasManyCorrectAnswers(YesNo.N);
        testFlashcard.setHasImageData(YesNo.Y);
        testFlashcard.setImageData(testBlobData);

        testFlashcardDTO = FlashcardDTO.builder()
                .id(1L)
                .deckId(1L)
                .prompt(PromptDTO.builder()
                        .id(10L)
                        .promptBody("Test Prompt")
                        .hasSingleAnswer(YesNo.Y)
                        .answers(Collections.singletonList(AnswerDTO.builder()
                                .id(100L)
                                .promptId(10L)
                                .answerBody("Test Answer")
                                .build()))
                        .build())
                .hasManyCorrectAnswers(YesNo.N)
                .hasImageData(YesNo.Y)
                .imageData(BlobDataDTO.builder()
                        .id(1000L)
                        .data(new byte[]{1, 2, 3})
                        .mimeType("image/png")
                        .build())
                .build();
    }

    @Test
    void getFlashcardsByDeck_ReturnsFlashcardDTOs() {
        when(flashcardRepository.findByDeckId(1L)).thenReturn(Collections.singletonList(testFlashcard));

        List<FlashcardDTO> result = flashcardService.getFlashcardsByDeck(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFlashcardDTO.getId(), result.get(0).getId());
        verify(flashcardRepository, times(1)).findByDeckId(1L);
    }

    @Test
    void getFlashcardsByHasManyCorrectAnswers_ReturnsFlashcardDTOs() {
        when(flashcardRepository.findByHasManyCorrectAnswers(YesNo.N)).thenReturn(Collections.singletonList(testFlashcard));

        List<FlashcardDTO> result = flashcardService.getFlashcardsByHasManyCorrectAnswers(YesNo.N);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFlashcardDTO.getId(), result.get(0).getId());
        verify(flashcardRepository, times(1)).findByHasManyCorrectAnswers(YesNo.N);
    }

    @Test
    void mapToDto_ValidFlashcard_ReturnsFlashcardDTO() {
        FlashcardDTO dto = flashcardService.mapToDto(testFlashcard);

        assertNotNull(dto);
        assertEquals(testFlashcardDTO.getId(), dto.getId());
        assertEquals(testFlashcardDTO.getDeckId(), dto.getDeckId());
        assertEquals(testFlashcardDTO.getPrompt().getId(), dto.getPrompt().getId());
        assertEquals(testFlashcardDTO.getHasManyCorrectAnswers(), dto.getHasManyCorrectAnswers());
        assertEquals(testFlashcardDTO.getHasImageData(), dto.getHasImageData());
        assertEquals(testFlashcardDTO.getImageData().getId(), dto.getImageData().getId());
    }

    @Test
    void mapToEntity_ValidFlashcardDTO_ReturnsFlashcard() {
        when(deckRepository.findById(1L)).thenReturn(Optional.of(testDeck));
        when(promptService.findById(anyLong())).thenReturn(testPrompt);
        doNothing().when(promptService).updateEntity(any(Prompt.class), any(PromptDTO.class));

        Flashcard flashcard = flashcardService.mapToEntity(testFlashcardDTO);

        assertNotNull(flashcard);
        assertEquals(testFlashcardDTO.getDeckId(), flashcard.getDeck().getId());
        assertEquals(testFlashcardDTO.getPrompt().getId(), flashcard.getPrompt().getId());
        assertEquals(testFlashcardDTO.getHasManyCorrectAnswers(), flashcard.getHasManyCorrectAnswers());
        assertEquals(testFlashcardDTO.getHasImageData(), flashcard.getHasImageData());
        assertEquals(testFlashcardDTO.getImageData().getData(), flashcard.getImageData().getData());
        verify(deckRepository, times(1)).findById(1L);
        verify(promptService, times(1)).findById(testFlashcardDTO.getPrompt().getId());
        verify(promptService, times(1)).updateEntity(testPrompt, testFlashcardDTO.getPrompt());
    }

    @Test
    void updateEntity_ExistingFlashcardAndDTO_UpdatesFlashcard() {
        Flashcard existingFlashcard = new Flashcard();
        existingFlashcard.setId(2L);
        existingFlashcard.setDeck(testDeck);
        existingFlashcard.setPrompt(new Prompt()); // Existing prompt
        existingFlashcard.setHasManyCorrectAnswers(YesNo.Y);
        existingFlashcard.setHasImageData(YesNo.N);
        existingFlashcard.setImageData(new BlobData()); // Existing image data

        FlashcardDTO updateDTO = FlashcardDTO.builder()
                .hasManyCorrectAnswers(YesNo.N)
                .hasImageData(YesNo.Y)
                .prompt(PromptDTO.builder().id(30L).promptBody("Updated Prompt").build())
                .imageData(BlobDataDTO.builder().data(new byte[]{4, 5, 6}).mimeType("image/jpeg").build())
                .build();

        doNothing().when(promptService).updateEntity(any(Prompt.class), any(PromptDTO.class));

        flashcardService.updateEntity(existingFlashcard, updateDTO);

        assertEquals(YesNo.N, existingFlashcard.getHasManyCorrectAnswers());
        assertEquals(YesNo.Y, existingFlashcard.getHasImageData());
        verify(promptService, times(1)).updateEntity(any(Prompt.class), any(PromptDTO.class));
        assertEquals(new byte[]{4, 5, 6}[0], existingFlashcard.getImageData().getData()[0]);
        assertEquals("image/jpeg", existingFlashcard.getImageData().getMimeType());
    }

    @Test
    void updateEntity_ExistingFlashcardAndDTO_RemovesPromptAndImageData() {
        Flashcard existingFlashcard = new Flashcard();
        existingFlashcard.setId(2L);
        existingFlashcard.setDeck(testDeck);
        existingFlashcard.setPrompt(testPrompt);
        existingFlashcard.setHasManyCorrectAnswers(YesNo.Y);
        existingFlashcard.setHasImageData(YesNo.Y);
        existingFlashcard.setImageData(testBlobData);

        FlashcardDTO updateDTO = FlashcardDTO.builder()
                .hasManyCorrectAnswers(YesNo.N)
                .hasImageData(YesNo.N)
                .prompt(null)
                .imageData(null)
                .build();

        doNothing().when(blobDataRepository).delete(any(BlobData.class));

        flashcardService.updateEntity(existingFlashcard, updateDTO);

        assertNull(existingFlashcard.getPrompt());
        assertNull(existingFlashcard.getImageData());
        verify(blobDataRepository, times(1)).delete(testBlobData);
    }
}
