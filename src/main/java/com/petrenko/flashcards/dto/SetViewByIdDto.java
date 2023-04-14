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
public class SetViewByIdDto {

    private String id;

    private String name;

    private String description;

    private String folderId;
    private String folderName;

    private String previousOrLastSetId;

    private String nextOrFirstSetId;

    public SetViewByIdDto() {
    }

    public SetViewByIdDto(String id, String name, String description, String folderId, String folderName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.folderId = folderId;
        this.folderName = folderName;
    }
}
