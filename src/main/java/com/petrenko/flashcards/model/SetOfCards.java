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
public class SetOfCards {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

    private LocalDateTime timeOfCreation;

    @ManyToOne
    private Folder folder;

    public SetOfCards() {
    }
    public SetOfCards(String id, String name, String description, LocalDateTime timeOfCreation, Folder folder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeOfCreation = timeOfCreation;
        this.folder = folder;
    }

    @PrePersist
    protected void prePersist() {
        timeOfCreation = LocalDateTime.now();
    }
}
