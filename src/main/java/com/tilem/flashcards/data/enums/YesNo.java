package com.tilem.flashcards.data.enums;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YesNo implements DbEnum {
	N("N", "Όχι"),
	Y("Y", "Ναι");

    private final String dbValue;
    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }

    public boolean getBoolean() {
        return this == Y;
    }

    public static YesNo getTypeFromBoolean(boolean yesNo) {
        if (yesNo) {
            return Y;
        } else {
            return N;
        }
    }

    public static YesNo getTypeFromString(String yesNoString) {
        if (StringUtils.isBlank(yesNoString)) {
            return null;
        }
	    if ("Y".equalsIgnoreCase(yesNoString)
			    || "Yes".equalsIgnoreCase(yesNoString)
			    || yesNoString.equalsIgnoreCase("Ναι")
			    || yesNoString.equalsIgnoreCase("NAI")) {
            return YesNo.Y;
	    } else if ("N".equalsIgnoreCase(yesNoString)
			    || "No".equalsIgnoreCase(yesNoString)
			    || yesNoString.equalsIgnoreCase("Όχι")
			    || yesNoString.equalsIgnoreCase("OXI")
			    || yesNoString.equalsIgnoreCase(
			    "ΟΧΙ")) {
		    return YesNo.N;
	    } else {
            return null;
        }
    }
}