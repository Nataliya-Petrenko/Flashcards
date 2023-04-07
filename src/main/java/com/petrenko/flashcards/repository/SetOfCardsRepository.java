package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SetOfCardsRepository extends CrudRepository<SetOfCards, String> {
    Optional<SetOfCards> findByName(String name);
}
