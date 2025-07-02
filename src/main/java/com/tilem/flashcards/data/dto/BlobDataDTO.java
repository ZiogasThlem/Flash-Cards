package com.tilem.flashcards.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlobDataDTO {
    private Long id;
    private byte[] data;
    private String mimeType;
}
