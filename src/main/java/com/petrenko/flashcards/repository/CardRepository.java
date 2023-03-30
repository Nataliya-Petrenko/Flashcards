package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, String> {
}
