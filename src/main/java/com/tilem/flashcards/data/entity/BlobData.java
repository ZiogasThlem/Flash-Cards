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
public class BlobData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blob_id")
    private long blobId;

    @Lob
    @Column(name = "blob_data", columnDefinition = "BYTEA")
    private byte[] blobData;

    @Column(name = "notes")
    private String notes;
}
