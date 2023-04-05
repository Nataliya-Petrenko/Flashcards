package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, String> {
    List<Card> getBySetOfCards(SetOfCards setOfCards);
}
