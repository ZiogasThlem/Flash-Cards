package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.DbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T extends DbEntity> extends JpaRepository<T, Long> {
}