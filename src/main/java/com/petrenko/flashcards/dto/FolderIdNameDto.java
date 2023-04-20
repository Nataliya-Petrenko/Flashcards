package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FolderIdNameDto {
    private String id;

    private String name;

    public FolderIdNameDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
