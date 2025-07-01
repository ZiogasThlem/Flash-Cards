package com.tilem.flashcards.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNo implements DbEnum {
    Y("Y", "Ναι"),
    N("N", "Όχι");

    private final String dbValue;
    private final String label;

}
