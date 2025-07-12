package com.tilem.flashcards.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blob_data")
public class BlobData extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blob_id")
    private Long id;

    @Lob
    @Column(name = "data", columnDefinition = "BYTEA")
    private byte[] data;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "notes")
    private String notes;

    @Override
    public String getEntityTitle() {
        return notes;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}