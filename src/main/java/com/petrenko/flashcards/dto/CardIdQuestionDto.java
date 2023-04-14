package com.petrenko.flashcards.dto;

import com.petrenko.flashcards.model.SetOfCards;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

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
