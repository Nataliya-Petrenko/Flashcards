package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.FolderIdNameDto;
import com.petrenko.flashcards.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, String> {

    Optional<Person> findPersonByEmail(String email);

    Optional<Person> findByEmail(String userName);



    @Query("""
            SELECT new com.petrenko.flashcards.dto.EditProfileDto(email, firstName, lastName)
            FROM Person
            WHERE id = :userId
            """)
    Optional<EditProfileDto> getEditProfileDtoByUserId(String userId);

    @Modifying
    @Query("UPDATE Person p SET p.email = :newEmail, p.firstName = :newFirstName, p.lastName = :newLastName WHERE p.id = :userId")
    void edit(String userId, String newEmail, String newFirstName, String newLastName);




//    @Modifying
//    @Query("UPDATE Person p SET p.password = :newPassword WHERE p.id = :userId")
//    void editPassword(String userId, String newPassword);
}