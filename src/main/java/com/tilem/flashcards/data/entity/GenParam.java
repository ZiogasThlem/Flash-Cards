package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gen_params")
public class GenParam extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "mandatory", length = 1)
    private YesNo mandatory;

    private String notes;

	@Override
    public String getEntityTitle() {
        return "GenParam";
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}