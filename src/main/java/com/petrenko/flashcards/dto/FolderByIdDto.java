package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FolderByIdDto {

    private String id;

    private String name;

    private String description;

    private String previousOrLastFolderId;

    private String nextOrFirstFolderId;


}
