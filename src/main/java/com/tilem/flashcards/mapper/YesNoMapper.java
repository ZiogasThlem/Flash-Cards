package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.enums.YesNo;
import org.springframework.stereotype.Component;

@Component
public class YesNoMapper {

    public Character asCharacter(YesNo yesNo) {
        return yesNo != null ? yesNo.getDbValue().charAt(0) : null;
    }

    public YesNo asYesNo(Character character) {
        if (character == null) {
            return null;
        }
        return YesNo.getTypeFromString(String.valueOf(character));
    }
}