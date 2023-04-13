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
            LEFT JOIN f.person p
            WHERE f.id = :folderId AND p.id = :userId
            """)
    List<SetIdNameDto> getByFolderId(String userId, String folderId);
}
