package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.SetNameFolderNameDto;
import com.petrenko.flashcards.dto.SetEditDto;
import com.petrenko.flashcards.dto.SetIdNameDto;
import com.petrenko.flashcards.dto.SetViewByIdDto;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SetOfCardsRepository extends CrudRepository<SetOfCards, String> {
    Optional<SetOfCards> findByName(String name);

    List<SetOfCards> getByFolder(Folder folder);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.SetIdNameDto(s.id, s.name)
            FROM SetOfCards s
            LEFT JOIN s.folder f
            WHERE f.id = :folderId
            """)
    List<SetIdNameDto> getByFolderId(String folderId);

    Optional<SetOfCards> findByNameAndFolderId(String setName, String folderId);

    @Query("""
            SELECT s.id
            FROM SetOfCards s
            WHERE s.timeOfCreation = (
              SELECT MAX(s2.timeOfCreation)
              FROM SetOfCards s2
              WHERE s2.timeOfCreation < (
                SELECT s3.timeOfCreation
                FROM SetOfCards s3
                LEFT JOIN s3.folder f
                WHERE f.id = :folderId AND s3.id = :setId
              )
            )
            """)
    Optional<String> getPreviousId(String folderId, String setId); // the oldest get null

    @Query("""
            SELECT s.id
            FROM SetOfCards s
            WHERE s.timeOfCreation = (
                SELECT MAX(s.timeOfCreation)
                From SetOfCards s
                LEFT JOIN s.folder f
                WHERE f.id = :folderId
                )
                 """)
    Optional<String> getLastId(String folderId);

    @Query("""
            SELECT s.id
            FROM SetOfCards s
            WHERE s.timeOfCreation = (
              SELECT MIN(s2.timeOfCreation)
              FROM SetOfCards s2
              WHERE s2.timeOfCreation > (
                SELECT s3.timeOfCreation
                FROM SetOfCards s3
                LEFT JOIN s3.folder f
                WHERE s3.id = :setId AND f.id = :folderId
              )
            )
            """)
    Optional<String> getNextId(String folderId, String setId);  // the newest get null

    @Query("""
            SELECT s.id
            FROM SetOfCards s
            WHERE s.timeOfCreation = (
                SELECT MIN(s.timeOfCreation)
                From SetOfCards s
                LEFT JOIN s.folder f
                WHERE f.id = :folderId
                )
                 """)
    Optional<String> getFirstId(String folderId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.SetViewByIdDto(
            s.id, s.name, s.description, f.id, f.name)
            FROM SetOfCards s
            LEFT JOIN s.folder f
            WHERE s.id = :setId
            """)
    Optional<SetViewByIdDto> getSetViewByIdDto(String setId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.SetEditDto(
            s.id, s.name, s.description, f.id, f.name)
            FROM SetOfCards s
            LEFT JOIN s.folder f
            WHERE s.id = :setId
            """)
    Optional<SetEditDto> getSetEditDto(String setId);

    @Modifying
    @Query("""
            UPDATE SetOfCards s
            SET s.description = :newDescription
            WHERE s.id = :setId
            """)
    void updateDescription(String setId, String newDescription);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.SetNameFolderNameDto(s.name, f.name)
            FROM SetOfCards s
            LEFT JOIN s.folder f
            WHERE s.id = :setId
                """)
    SetNameFolderNameDto getSetNameFolderNameDtoBySetId(String setId);

    @Query("""
            SELECT s.name
            FROM SetOfCards s
            LEFT JOIN s.folder f
            WHERE f.id = :folderId
                """)
    List<String> getSetsNameByFolderId(String folderId);
}
