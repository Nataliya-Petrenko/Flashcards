package com.petrenko.flashcards.dto;

import com.petrenko.flashcards.model.Folder;
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
public class SetIdNameDto {
    private String id;
    private String name;

    public SetIdNameDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
