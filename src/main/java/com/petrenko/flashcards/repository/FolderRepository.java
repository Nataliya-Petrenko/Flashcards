package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.FolderIdNameDto;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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
}
