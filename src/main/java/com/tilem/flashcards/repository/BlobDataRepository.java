package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.BlobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlobDataRepository extends JpaRepository<BlobData, Long> {
}