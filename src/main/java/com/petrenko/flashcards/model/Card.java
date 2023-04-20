package com.petrenko.flashcards.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Type(type = "text")
    private String question;

    @Type(type = "text")
    private String shortAnswer;

    @Type(type = "text")
    private String longAnswer;

    @ManyToOne
    private SetOfCards setOfCards;

    private LocalDateTime timeOfCreation;

    @PrePersist
    protected void prePersist() {
        timeOfCreation = LocalDateTime.now();
    }
}
