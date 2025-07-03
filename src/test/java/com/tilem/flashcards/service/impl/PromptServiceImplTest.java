package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptServiceImplTest {

    @Mock
    private PromptRepository promptRepository;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private PromptServiceImpl promptService;

    private Prompt testPrompt;
    private PromptDTO testPromptDTO;
    private Answer testAnswer1;
    private AnswerDTO testAnswerDTO1;

    @BeforeEach
    void setUp() {
        testPrompt = new Prompt();
        testPrompt.setId(1L);
        testPrompt.setPromptBody("Test Prompt Body");
        testPrompt.setHasSingleAnswer(YesNo.Y);

        testAnswer1 = new Answer();
        testAnswer1.setId(10L);
        testAnswer1.setPrompt(testPrompt);
        testAnswer1.setAnswerBody("Test Answer Body 1");
        testAnswer1.setNotes("Notes 1");

        testPrompt.setAnswers(new ArrayList<>(Arrays.asList(testAnswer1)));

        testAnswerDTO1 = AnswerDTO.builder()
                .id(10L)
                .promptId(1L)
                .answerBody("Test Answer Body 1")
                .notes("Notes 1")
                .build();

        testPromptDTO = PromptDTO.builder()
                .id(1L)
                .promptBody("Test Prompt Body")
                .hasSingleAnswer(YesNo.Y)
                .answers(Arrays.asList(testAnswerDTO1))
                .build();
    }

    @Test
    void mapToDto_ValidPrompt_ReturnsPromptDTO() {
        PromptDTO dto = promptService.mapToDto(testPrompt);

        assertNotNull(dto);
        assertEquals(testPrompt.getId(), dto.getId());
        assertEquals(testPrompt.getPromptBody(), dto.getPromptBody());
        assertEquals(testPrompt.getHasSingleAnswer(), dto.getHasSingleAnswer());
        assertNotNull(dto.getAnswers());
        assertEquals(1, dto.getAnswers().size());
        assertEquals(testAnswerDTO1.getId(), dto.getAnswers().get(0).getId());
        assertEquals(testAnswerDTO1.getPromptId(), dto.getAnswers().get(0).getPromptId());
        assertEquals(testAnswerDTO1.getAnswerBody(), dto.getAnswers().get(0).getAnswerBody());
        assertEquals(testAnswerDTO1.getNotes(), dto.getAnswers().get(0).getNotes());
    }

    @Test
    void mapToEntity_ValidPromptDTO_ReturnsPrompt() {
        when(answerService.mapToEntity(any(AnswerDTO.class))).thenReturn(testAnswer1);

        Prompt prompt = promptService.mapToEntity(testPromptDTO);

        assertNotNull(prompt);
        assertEquals(testPromptDTO.getPromptBody(), prompt.getPromptBody());
        assertEquals(testPromptDTO.getHasSingleAnswer(), prompt.getHasSingleAnswer());
        assertNotNull(prompt.getAnswers());
        assertEquals(1, prompt.getAnswers().size());
        assertEquals(testAnswer1, prompt.getAnswers().get(0));
        assertEquals(prompt, prompt.getAnswers().get(0).getPrompt());
        verify(answerService, times(1)).mapToEntity(testAnswerDTO1);
    }

    @Test
    void updateEntity_ExistingPromptAndDTO_UpdatesPromptAndAnswers() {
        Prompt existingPrompt = new Prompt();
        existingPrompt.setId(2L);
        existingPrompt.setPromptBody("Old Prompt Body");
        existingPrompt.setHasSingleAnswer(YesNo.N);

        Answer existingAnswer = new Answer();
        existingAnswer.setId(20L);
        existingAnswer.setPrompt(existingPrompt);
        existingAnswer.setAnswerBody("Old Answer Body");
        existingAnswer.setNotes("Old Notes");
        existingPrompt.setAnswers(new ArrayList<>(Arrays.asList(existingAnswer)));

        PromptDTO updateDTO = PromptDTO.builder()
                .promptBody("New Prompt Body")
                .hasSingleAnswer(YesNo.Y)
                .answers(Arrays.asList(
                        AnswerDTO.builder().id(20L).answerBody("Updated Answer Body").notes("Updated Notes").build(),
                        AnswerDTO.builder().answerBody("New Answer Body").notes("New Notes").build()
                ))
                .build();

        when(answerService.mapToEntity(any(AnswerDTO.class))).thenReturn(new Answer());
        doNothing().when(answerService).updateEntity(any(Answer.class), any(AnswerDTO.class));

        promptService.updateEntity(existingPrompt, updateDTO);

        assertEquals("New Prompt Body", existingPrompt.getPromptBody());
        assertEquals(YesNo.Y, existingPrompt.getHasSingleAnswer());
        assertEquals(2, existingPrompt.getAnswers().size());

        // Verify existing answer updated
        verify(answerService, times(1)).updateEntity(any(Answer.class), argThat(dto -> dto.getId().equals(20L) && dto.getAnswerBody().equals("Updated Answer Body")));

        // Verify new answer added
        verify(answerService, times(1)).mapToEntity(argThat(dto -> dto.getAnswerBody().equals("New Answer Body")));
    }

    @Test
    void updateEntity_ExistingPromptAndDTO_RemovesAnswers() {
        Prompt existingPrompt = new Prompt();
        existingPrompt.setId(2L);
        existingPrompt.setPromptBody("Old Prompt Body");
        existingPrompt.setHasSingleAnswer(YesNo.N);

        Answer existingAnswer = new Answer();
        existingAnswer.setId(20L);
        existingAnswer.setPrompt(existingPrompt);
        existingAnswer.setAnswerBody("Old Answer Body");
        existingAnswer.setNotes("Old Notes");
        existingPrompt.setAnswers(new ArrayList<>(Arrays.asList(existingAnswer)));

        PromptDTO updateDTO = PromptDTO.builder()
                .promptBody("New Prompt Body")
                .hasSingleAnswer(YesNo.Y)
                .answers(new ArrayList<>())
                .build();

        promptService.updateEntity(existingPrompt, updateDTO);

        assertEquals("New Prompt Body", existingPrompt.getPromptBody());
        assertEquals(YesNo.Y, existingPrompt.getHasSingleAnswer());
        assertTrue(existingPrompt.getAnswers().isEmpty());
    }

    @Test
    void updateEntity_ExistingPromptAndDTO_NoAnswersInDTO() {
        Prompt existingPrompt = new Prompt();
        existingPrompt.setId(2L);
        existingPrompt.setPromptBody("Old Prompt Body");
        existingPrompt.setHasSingleAnswer(YesNo.N);

        Answer existingAnswer = new Answer();
        existingAnswer.setId(20L);
        existingAnswer.setPrompt(existingPrompt);
        existingAnswer.setAnswerBody("Old Answer Body");
        existingAnswer.setNotes("Old Notes");
        existingPrompt.setAnswers(new ArrayList<>(Arrays.asList(existingAnswer)));

        PromptDTO updateDTO = PromptDTO.builder()
                .promptBody("New Prompt Body")
                .hasSingleAnswer(YesNo.Y)
                .build(); // No answers in DTO

        promptService.updateEntity(existingPrompt, updateDTO);

        assertEquals("New Prompt Body", existingPrompt.getPromptBody());
        assertEquals(YesNo.Y, existingPrompt.getHasSingleAnswer());
        assertTrue(existingPrompt.getAnswers().isEmpty());
    }
}
