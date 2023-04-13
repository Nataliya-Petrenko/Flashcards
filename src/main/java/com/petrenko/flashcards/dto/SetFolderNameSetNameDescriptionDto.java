package com.petrenko.flashcards.dto;

import com.petrenko.flashcards.model.Folder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class SetFolderNameSetNameDescriptionDto {

    private String  folderName;

    private String name;

    private String description;

}
