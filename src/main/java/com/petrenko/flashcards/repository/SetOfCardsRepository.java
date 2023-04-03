package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.repository.CrudRepository;

public interface SetOfCardsRepository extends CrudRepository<SetOfCards, String> {
}
