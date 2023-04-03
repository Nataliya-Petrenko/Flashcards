package com.petrenko.flashcards.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
//@NoArgsConstructor
public class KeyWord {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

//    @NotBlank(message = "Field is mandatory")
    private String name;


//    public KeyWord(String name) {
//        this.name = name;
//    }

}
