package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.FolderByIdDto;
import com.petrenko.flashcards.dto.FolderIdNameDescriptionDto;
import com.petrenko.flashcards.dto.FolderIdNameDto;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends CrudRepository<Folder, String> {
    Optional<Folder> findByName(String name);

    @Query("""
            SELECT new com.petrenko.flashcards.model.Folder(
            f.id, f.name, f.description, f.timeOfCreation, f.person
            )
            FROM Folder as f
            LEFT JOIN f.person as p
            WHERE f.name = :newName AND p.id = :userId
            """)
    Optional<Folder> findByUserIdAndName(String userId, String newName);

    List<Folder> findByPersonId(String userId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.FolderIdNameDto(f.id, f.name)
            FROM Folder f
            LEFT JOIN f.person p
            WHERE p.id = :userId
            """)
    List<FolderIdNameDto> getFoldersIdNameDtoByPersonId(String userId);

//    @Query("""
//        SELECT new com.petrenko.flashcards.dto.FolderByIdDto(f.id, f.name, f.description)
//        FROM Folder f
//        LEFT JOIN f.person p
//        WHERE p.id = :userId AND f.id = :folderId
//            """)
//    FolderByIdDto getFolderByIdDto(String userId, String folderId); // todo delete

    @Query("""
            SELECT f.id
            FROM Folder f
            WHERE f.timeOfCreation = (
              SELECT MAX(f2.timeOfCreation)
              FROM Folder f2
              WHERE f2.timeOfCreation < (
                SELECT f3.timeOfCreation
                FROM Folder f3
                LEFT JOIN f3.person p
                WHERE p.id = :userId AND f3.id = :folderId 
              )
            )
            """)
    Optional<String> getPreviousId(String userId, String folderId); // the oldest get null

    @Query("""
            SELECT f.id
            FROM Folder f
            WHERE f.timeOfCreation = (
                SELECT MAX(f.timeOfCreation)
                From Folder f
                LEFT JOIN f.person p
                WHERE p.id = :userId
                )
                 """)
    Optional<String> getLastId(String userId);

    @Query("""
            SELECT f.id
            FROM Folder f
            WHERE f.timeOfCreation = (
              SELECT MIN(f2.timeOfCreation)
              FROM Folder f2
              WHERE f2.timeOfCreation > (
                SELECT f3.timeOfCreation
                FROM Folder f3
                LEFT JOIN f3.person p
                WHERE f3.id = :folderId AND p.id = :userId
              )
            )
            """)
    Optional<String> getNextId(String userId, String folderId);  // the newest get null

    @Query("""
            SELECT f.id
            FROM Folder f
            WHERE f.timeOfCreation = (
                SELECT MIN(f.timeOfCreation)
                From Folder f
                LEFT JOIN f.person p
                WHERE p.id = :userId
                )
                 """)
    Optional<String> getFirstId(String userId);

    @Query("""
        SELECT new com.petrenko.flashcards.dto.FolderIdNameDescriptionDto(f.id, f.name, f.description)
        FROM Folder f
        LEFT JOIN f.person p
        WHERE p.id = :userId AND f.id = :folderId
            """)
    Optional<FolderIdNameDescriptionDto> getFolderIdNameDescriptionDto(String userId, String folderId);

    @Query("""
            SELECT f.id
            FROM Folder as f
            LEFT JOIN f.person as p
            WHERE f.name = :newName AND p.id = :userId
            """)
    Optional<String> findIdByUserIdAndName(String userId, String newName);

    @Modifying
    @Query("""
            UPDATE Folder f SET f.name = :newName, f.description = :newDescription 
            WHERE f.person.id = :userId
            """)
    void update(String userId, String newName, String newDescription);
}
