package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.KeyWord;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.KeyWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyWordService {

    private final KeyWordRepository keyWordRepository;

    @Autowired
    public KeyWordService(KeyWordRepository keyWordRepository) {
        this.keyWordRepository = keyWordRepository;
    }

//    public KeyWord getById(final String id) {
//        Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
//        System.out.println(card);
//        return card;
//    }

    public void save(KeyWord keyWord) {
        keyWordRepository.save(keyWord);
    }

    public void deleteById(String id) {
        keyWordRepository.deleteById(id);
    }

}
