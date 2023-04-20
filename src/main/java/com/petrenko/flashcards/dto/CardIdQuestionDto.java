package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CardIdQuestionDto {
    private String id;

    private String question;

    public CardIdQuestionDto(String id, String question) {
        this.id = id;
        this.question = question;
    }
}
