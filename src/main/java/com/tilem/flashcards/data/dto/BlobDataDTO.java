package com.tilem.flashcards.data.dto;

public record BlobDataDTO(Long id, byte[] data, String mimeType) {
}