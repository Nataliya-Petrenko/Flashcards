package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.SetOfCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetOfCardsService {

    private final SetOfCardsRepository setOfCardsRepository;

    @Autowired
    public SetOfCardsService(SetOfCardsRepository setOfCardsRepository) {
        this.setOfCardsRepository = setOfCardsRepository;
    }

//    public SetOfCards getById(final String id) {
//        SetOfCards setOfCards = setOfCardsRepository.findById(id).orElseThrow(IllegalArgumentException::new);
//        System.out.println(setOfCards);
//        return setOfCards;
//    }

    public void save(SetOfCards setOfCards) {
        setOfCardsRepository.save(setOfCards);
    }

//    public void deleteById(String id) {
//        cardRepository.deleteById(id);
//    }

}
