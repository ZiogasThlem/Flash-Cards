package com.tilem.flashcards.service;

import java.util.List;

public interface GenericService<T, U> {
    List<U> findAll();
    T findById(Long id);
    U create(U dto);
    U update(Long id, U dto);
    void delete(Long id);
}
