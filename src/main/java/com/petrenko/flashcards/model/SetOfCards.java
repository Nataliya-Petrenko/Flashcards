package com.petrenko.flashcards.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
//@NoArgsConstructor
public class SetOfCards {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    //    @NotBlank(message = "Field is mandatory")
    @Type(type = "text")
    private String name;

//    @OneToMany
////            (cascade = CascadeType.ALL)
//    private List<Card> cards;

    @Column(name = "study_priority")
    private StudyPriority studyPriority;

    @Column(name = "description_of_set")
    private String descriptionOfSet;

//    public SetOfCards(final String name) {
//        this.name = name;
//    }
}
