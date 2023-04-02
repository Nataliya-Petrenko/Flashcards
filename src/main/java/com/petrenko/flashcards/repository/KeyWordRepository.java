package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.KeyWord;
import org.springframework.data.repository.CrudRepository;

public interface KeyWordRepository extends CrudRepository<KeyWord, String> {
}
