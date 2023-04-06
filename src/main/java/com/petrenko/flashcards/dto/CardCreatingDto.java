package com.petrenko.flashcards.dto;

import com.petrenko.flashcards.model.KnowledgeLevel;
import com.petrenko.flashcards.model.StudyPriority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCreatingDto {

    private String question;

    private String shortAnswer;

    private String longAnswer;

    private String setOfCardsName;

    private String keyWordsString;

    private String studyPriority;

    private String knowledgeLevel;
}
