package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditProfileDto {

    private String email;

    private String firstName;

    private String lastName;

    private String avatar;

    public EditProfileDto(String email, String firstName, String lastName, String avatar) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }
}
