package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.UsersInfoDto;
import com.petrenko.flashcards.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, String> {

    Optional<Person> findPersonByEmail(String email);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.EditProfileDto(email, firstName, lastName, avatar)
            FROM Person
            WHERE id = :userId
            """)
    Optional<EditProfileDto> getEditProfileDtoByUserId(String userId);

    @Modifying
    @Query("""
            UPDATE Person p SET p.email = :newEmail, p.firstName = :newFirstName, p.lastName = :newLastName, p.avatar = :newAvatar
            WHERE p.id = :userId
            """)
    void update(String userId, String newEmail, String newFirstName, String newLastName, String newAvatar);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.UsersInfoDto(id, email, firstName, lastName, enable, role)
            FROM Person
            """)
    List<UsersInfoDto> getUsersInfoDto();

    @Query("""
            SELECT new com.petrenko.flashcards.dto.UsersInfoDto(id, email, firstName, lastName, enable, role)
            FROM Person
            WHERE id = :userId
            """)
    Optional<UsersInfoDto> getUserInfoDto(String userId);

    @Query("""
            SELECT COUNT(p.id)
            FROM Person p
            """)
    long count();
}
