package com.petrenko.flashcards.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "Field is mandatory")
    @Type(type = "text")
    private String question;

    @Type(type = "text")
    @Column(name = "short_answer")
    private String shortAnswer;

    @Type(type = "text")
    @Column(name = "long_answer")
    private String longAnswer;

    @ManyToOne
    private SetOfCards setOfCards;

    @Column(name = "key_words")
    @ManyToMany
    private List<KeyWord> keyWords;

    @Column(name = "study_priority")
    private StudyPriority studyPriority;

    @Column(name = "knowledge_level")
    private KnowledgeLevel knowledgeLevel;

    private Boolean know;
}
