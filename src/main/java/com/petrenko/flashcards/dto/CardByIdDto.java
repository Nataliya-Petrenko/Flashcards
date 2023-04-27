package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
public class CardByIdDto {
    private String id;

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String setOfCardsId;

    private String setOfCardsName;

    private String folderId;
    private String folderName;

    private String previousOrLastCardId;

    private String nextOrFirstCardId;

    public CardByIdDto(String id, String question, String shortAnswer, String longAnswer,
                       String setOfCardsId, String setOfCardsName, String folderName) {
        this.id = id;
        this.question = question;
        this.shortAnswer = shortAnswer;
        this.longAnswer = longAnswer;
        this.setOfCardsId = setOfCardsId;
        this.setOfCardsName = setOfCardsName;
        this.folderName = folderName;
    }
}
