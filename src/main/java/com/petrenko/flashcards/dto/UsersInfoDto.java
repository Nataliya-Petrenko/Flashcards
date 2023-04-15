package com.petrenko.flashcards.dto;

import com.petrenko.flashcards.model.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class UsersInfoDto {
    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean enable;

    private Role role;

    public UsersInfoDto(String id, String email, String firstName, String lastName,
                        Boolean enable, Role role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enable = enable;
        this.role = role;
    }
}
