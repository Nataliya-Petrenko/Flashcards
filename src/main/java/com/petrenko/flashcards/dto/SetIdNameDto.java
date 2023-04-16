package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
