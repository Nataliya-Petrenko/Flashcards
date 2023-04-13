package com.petrenko.flashcards.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.UniqueElements;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class RegistrationPersonDto {

    @NotBlank(message = "Field is mandatory")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, " +
                    "and be at least 8 characters long.")
    private String password;

//    @UniqueElements(message = "This email already exist") // not work with it
    @NotBlank(message = "Field is mandatory")
    @Pattern(regexp = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Email incorrect")
    private String email;

    private String firstName;

    private String lastName;

}
