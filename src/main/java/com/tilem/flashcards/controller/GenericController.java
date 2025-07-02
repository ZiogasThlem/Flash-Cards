package com.tilem.flashcards.controller;

import com.tilem.flashcards.service.GenericService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<T, U> {

    private final GenericService<T, U> service;

    public GenericController(GenericService<T, U> service) {
        this.service = service;
    }

    @GetMapping
    public List<U> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public U getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public U create(@RequestBody U dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public U update(@PathVariable Long id, @RequestBody U dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
