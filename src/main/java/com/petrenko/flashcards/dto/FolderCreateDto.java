package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FolderCreateDto {

    private String name;

    private String description;

}
