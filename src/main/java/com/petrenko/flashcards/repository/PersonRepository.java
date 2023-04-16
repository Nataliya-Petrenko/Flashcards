package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.UsersInfoDto;
import com.petrenko.flashcards.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, String> {

    Optional<Person> findPersonByEmail(String email);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.EditProfileDto(email, firstName, lastName)
            FROM Person
            WHERE id = :userId
            """)
    Optional<EditProfileDto> getEditProfileDtoByUserId(String userId);

    @Modifying
    @Query("""
            UPDATE Person p SET p.email = :newEmail, p.firstName = :newFirstName, p.lastName = :newLastName
            WHERE p.id = :userId
            """)
    void update(String userId, String newEmail, String newFirstName, String newLastName);

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

    @Query("""
            SELECT p.enable
            FROM Person p
            WHERE id = :userId
            """)
    boolean getEnable(String userId);

    @Modifying
    @Query("""
            UPDATE Person SET enable = :isEnable
            WHERE id = :userId
            """)
    void blockUser(String userId, boolean isEnable);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.UsersInfoDto(id, email, firstName, lastName, enable, role)
            FROM Person
            WHERE firstName LIKE %:search% OR lastName LIKE %:search%
            """)
    List<UsersInfoDto> getBySearch(@Param("search") String search);
}
