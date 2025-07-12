package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.service.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController extends GenericController<Answer, AnswerDTO> {

    public AnswerController(AnswerService answerService) {
        super(answerService);
    }

    @GetMapping
    public ResponseEntity<List<AnswerDTO>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<AnswerDTO> create(@RequestBody AnswerDTO dto) {
        return super.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerDTO> update(@PathVariable Long id, @RequestBody AnswerDTO dto) {
        return super.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }
}