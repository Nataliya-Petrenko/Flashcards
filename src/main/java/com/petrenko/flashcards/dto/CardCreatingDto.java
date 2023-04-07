package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCreatingDto {

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String setOfCardsName;
}
