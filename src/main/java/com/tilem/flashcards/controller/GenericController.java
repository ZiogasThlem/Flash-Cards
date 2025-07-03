package com.tilem.flashcards.controller;

import com.tilem.flashcards.service.GenericService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<T, U> {

    private final GenericService<T, U> service;

    public GenericController(GenericService<T, U> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<U>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<U> getById(@PathVariable Long id) {
        T entity = service.findById(id);
        return ResponseEntity.ok(mapToDto(entity));
    }

    protected abstract U mapToDto(T entity);

    @PostMapping
    public ResponseEntity<U> create(@RequestBody U dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<U> update(@PathVariable Long id, @RequestBody U dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
