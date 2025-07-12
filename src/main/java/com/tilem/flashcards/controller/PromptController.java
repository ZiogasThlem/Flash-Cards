package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.service.PromptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prompts")
public class PromptController extends GenericController<Prompt, PromptDTO> {

	public PromptController(PromptService promptService) {
		super(promptService);
    }

	@GetMapping
	public ResponseEntity<List<PromptDTO>> getAll() {
		return super.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PromptDTO> getById(@PathVariable Long id) {
		return super.getById(id);
	}

	@PostMapping
	public ResponseEntity<PromptDTO> create(@RequestBody PromptDTO dto) {
		return super.create(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PromptDTO> update(@PathVariable Long id, @RequestBody PromptDTO dto) {
		return super.update(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return super.delete(id);
	}
}