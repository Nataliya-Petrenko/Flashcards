package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getById(final String id) {
        Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        System.out.println("Service: getById: " + card);
        return card;
    }

    public Card newCard(SetOfCards setOfCards) {
        Card card = new Card();
        card.setSetOfCards(setOfCards);
        card.setQuestion("question");
        save(card);
        System.out.println("Service: Created card " + card);
        return card;
    }

    public void save(Card card) {
        cardRepository.save(card);
        System.out.println("Service: Saved card " + card.getId());
    }

//    public void deleteById(String id) {
//        cardRepository.deleteById(id);
//    }

    public List<Card> getBySet(SetOfCards setOfCards) {
        List<Card> bySetOfCards = cardRepository.getBySetOfCards(setOfCards);
        System.out.println("Service: getBySet: " + bySetOfCards);
        return bySetOfCards;
    }
}
