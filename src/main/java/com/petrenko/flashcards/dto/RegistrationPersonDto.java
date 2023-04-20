package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistrationPersonDto {

    private String password;

    private String email;

    private String firstName;

    private String lastName;

}
