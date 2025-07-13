package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.YesNo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenParamDTO {

    private Long id;
    private String value;
    private YesNo mandatory;
    private String notes;
}