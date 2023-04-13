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

    //    @NotBlank(message = "Field is mandatory")
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

    private LocalDateTime timeOfCreation;

    @ManyToOne
    private Folder folder;

    @PrePersist
    protected void prePersist() {
        timeOfCreation = LocalDateTime.now();
    }
}
