package com.petrenko.flashcards.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

//    @NotBlank(message = "Field is mandatory")
    @Type(type = "text")
    private String question;

    @Type(type = "text")
    @Column(name = "short_answer")
    private String shortAnswer;

    @Type(type = "text")
    @Column(name = "long_answer")
    private String longAnswer;

    //    @NotBlank(message = "Field is mandatory")
    @ManyToOne
    private SetOfCards setOfCards;

    private LocalDateTime timeOfCreation;

    @PrePersist
    protected void prePersist() {
        timeOfCreation = LocalDateTime.now();
    }
}
