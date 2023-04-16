package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CardCreatingDto {

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String folderName;

    private String setOfCardsName;

}
