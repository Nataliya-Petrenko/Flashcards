package com.petrenko.flashcards.dto;

import com.petrenko.flashcards.model.KeyWord;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardViewByIdDto {

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String setOfCardsName;

    private List<KeyWord> keyWords;

    private String previousCardId;

    private String nextCardId;

}
