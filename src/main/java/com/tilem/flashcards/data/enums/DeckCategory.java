package com.tilem.flashcards.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeckCategory implements DbEnum {

    GEN("GEN", "Γενικό"),
    FL("FL", "Ξένη Γλώσσα")
    ;

    private final String dbValue;
    private final String label;

}
