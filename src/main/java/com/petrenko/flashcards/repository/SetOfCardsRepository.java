package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.SetIdNameDto;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
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

//    @Query("""
//            SELECT new com.petrenko.flashcards.model.SetOfCards(
//            f.id, f.name, f.description, f.timeOfCreation, f.person
//            )
//            FROM Folder as f
//            LEFT JOIN f.person as p
//            WHERE f.name = :newName AND p.id = :userId
//            """)
//    Optional<SetOfCards> getSetByFolderIdWith(String folderId, String setName);

    Optional<SetOfCards> findByNameAndFolderId(String setName, String folderId);
}
