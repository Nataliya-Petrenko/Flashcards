package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Setter
@Getter
@ToString
public class FolderCreateDto {

    //    @NotBlank(message = "Field is mandatory")
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

}
