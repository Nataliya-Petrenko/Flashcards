package com.petrenko.flashcards.repository;

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
}
