package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CardEditingDto {
    private String id;

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String setOfCardsName;

    public CardEditingDto(String id, String question, String shortAnswer, String longAnswer, String setOfCardsName) {
        this.id = id;
        this.question = question;
        this.shortAnswer = shortAnswer;
        this.longAnswer = longAnswer;
        this.setOfCardsName = setOfCardsName;
    }
}