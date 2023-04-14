package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@ToString
public class CardEditDto {
    private String id;

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String setOfCardsId;

    private String setOfCardsName;

    private String folderId;
    private String folderName;


    public CardEditDto(String id, String question, String shortAnswer, String longAnswer,
                       String setOfCardsId, String setOfCardsName, String folderId, String folderName) {
        this.id = id;
        this.question = question;
        this.shortAnswer = shortAnswer;
        this.longAnswer = longAnswer;
        this.setOfCardsId = setOfCardsId;
        this.setOfCardsName = setOfCardsName;
        this.folderId = folderId;
        this.folderName = folderName;
    }
}
