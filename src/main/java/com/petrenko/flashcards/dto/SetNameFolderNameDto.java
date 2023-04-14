package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SetNameFolderNameDto {
    private String setName;

    private String  folderName;

    public SetNameFolderNameDto(String setName, String folderName) {
        this.setName = setName;
        this.folderName = folderName;
    }
}
