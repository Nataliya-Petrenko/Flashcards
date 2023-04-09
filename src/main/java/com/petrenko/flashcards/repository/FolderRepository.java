package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FolderRepository extends CrudRepository<Folder, String> {
//    Optional<Folder> findByName(String name);
}
