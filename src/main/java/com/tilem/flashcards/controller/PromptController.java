package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.service.PromptService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prompts")
public class PromptController extends GenericController<Prompt, PromptDTO> {

    public PromptController(PromptService promptService) {
        super(promptService);
    }
}
