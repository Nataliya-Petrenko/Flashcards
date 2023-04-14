package com.petrenko.flashcards.repository;

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
            SELECT s.id
            FROM SetOfCards as s
            JOIN s.folder as f
            WHERE s.name = :name AND f.id = :folderId
            """)
    Optional<String> getIdFromFolderByName(String name, String folderId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.SetIdNameDto(s.id, s.name)
            FROM SetOfCards s
            LEFT JOIN s.folder f
            WHERE f.id = :folderId
            """)
    List<SetIdNameDto> getByFolderId(String folderId);

    @Query("""
            SELECT s.id
            FROM SetOfCards as s
            LEFT JOIN s.folder f
            LEFT JOIN f.person as p
            WHERE p.id = :userId AND f.name = :folderName AND s.name = :setName
            """)
    Optional<String> getSetIdWithNameInFolder(String userId, String folderName, String setName);

    Optional<SetOfCards> findByNameAndFolderId(String setName, String folderId);

//    @Query("""
//            SELECT s.id
//            FROM SetOfCards s
//            JOIN s.folder f
//            WHERE s.timeOfCreation = (
//                SELECT MAX(s2.timeOfCreation)
//                FROM SetOfCards s2
//                JOIN s2.folder f2
//                WHERE f2.id = f.id
//                AND s2.timeOfCreation < (
//                    SELECT s3.timeOfCreation
//                    FROM SetOfCards s3
//                    JOIN s3.folder f3
//                    WHERE f3.id = (
//                        SELECT f4.id
//                        FROM SetOfCards s4
//                        JOIN s4.folder f4
//                        WHERE s4.id = :setId
//                    )
//                )
//            )
//            """)
//    Optional<String> getPreviousId(String setId); // the oldest get null
//
//    @Query("""
//            SELECT s.id
//            FROM SetOfCards s
//            JOIN s.folder f
//            WHERE s.timeOfCreation = (
//                SELECT MAX(s2.timeOfCreation)
//                FROM SetOfCards s2
//                JOIN s2.folder f2
//                WHERE f2.id = f.id
//                )
//                AND f.id = (
//                    SELECT f2.id
//                    FROM SetOfCards s4
//                    JOIN s4.folder f2
//                    WHERE s4.id = :setId
//                )
//            """)
//    Optional<String> getLastId(String setId);
//
//    @Query("""
//            SELECT s.id
//            FROM SetOfCards s
//            JOIN s.folder f
//            WHERE s.timeOfCreation = (
//                SELECT MIN(s2.timeOfCreation)
//                FROM SetOfCards s2
//                JOIN s2.folder f2
//                WHERE f2.id = f.id
//                AND s2.timeOfCreation > (
//                    SELECT s3.timeOfCreation
//                    FROM SetOfCards s3
//                    JOIN s3.folder f3
//                    WHERE f3.id = (
//                        SELECT f4.id
//                        FROM SetOfCards s4
//                        JOIN s4.folder f4
//                        WHERE s4.id = :setId
//                    )
//                )
//            )
//            """)
//    Optional<String> getNextId(String setId); // the newest get null
//
//    @Query("""
//            SELECT s.id
//            FROM SetOfCards s
//            JOIN s.folder f
//            WHERE s.timeOfCreation = (
//                SELECT MIN(s2.timeOfCreation)
//                FROM SetOfCards s2
//                JOIN s2.folder f2
//                WHERE f2.id = f.id
//                )
//                AND f.id = (
//                    SELECT f2.id
//                    FROM SetOfCards s4
//                    JOIN s4.folder f2
//                    WHERE s4.id = :setId
//                )
//            """)
//    Optional<String> getFirstId(String setId);

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
    //
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
}
