package com.petrenko.flashcards.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class Folder {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    //    @NotBlank(message = "Field is mandatory")
//    @Type(type = "text")
    private String name;

//    @Column(name = "description_of_set")
    private String description;

    private LocalDateTime timeOfCreation;

    @ManyToOne
    private Person person;

    public Folder() {
    }

    public Folder(String id, String name, String description, LocalDateTime timeOfCreation, Person person) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeOfCreation = timeOfCreation;
        this.person = person;
    }

    @PrePersist
    protected void prePersist() {
        timeOfCreation = LocalDateTime.now();
    }
}
