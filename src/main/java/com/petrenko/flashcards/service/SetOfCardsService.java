package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.SetOfCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class SetOfCardsService {

    private final SetOfCardsRepository setOfCardsRepository;
    private final CardService cardService;
    private final CardRepository cardRepository;

    @Autowired
    public SetOfCardsService(final SetOfCardsRepository setOfCardsRepository, final CardService cardService, CardRepository cardRepository) {
        this.setOfCardsRepository = setOfCardsRepository;
        this.cardService = cardService;
        this.cardRepository = cardRepository;
    }

//    public SetOfCards getById(final String id) {
//        SetOfCards setOfCards = setOfCardsRepository.findById(id).orElseThrow(IllegalArgumentException::new);
//        System.out.println(setOfCards);
//        return setOfCards;
//    }

    @Transactional
    public SetOfCards newSet() {
        SetOfCards setOfCards = new SetOfCards();
        setOfCards.setName("NewSet 0");
//        setOfCardsService.save(setOfCards);

//        List<Card> cardList = new LinkedList<>();
//        System.out.println("New List<Card>");
        cardService.newCard(setOfCards);
        cardService.newCard(setOfCards);
//        for (int i = 0; i < 4; i++) {
//
//            System.out.println("Start creating new Card " + i);
//            Card card = new Card();
//            card.setQuestion(("Question ").repeat(5));
//////            card.setShortAnswer(("ShortAnswer ").repeat(10));
//////            card.setLongAnswer(("LongAnswer ").repeat(50));
//            card.setSetOfCards(setOfCards);
//
////            List<KeyWord> keyWords = new LinkedList<>();
////            for (int j = 0; j < 2; j++) {
////                KeyWord keyWord = new KeyWord();
////                keyWord.setName("key word " + j);
////                keyWords.add(keyWord);
////                keyWordService.save(keyWord);
////                System.out.println("New key word " + keyWord);
////            }
////            card.setKeyWords(keyWords);
//
//            System.out.println("Card before saving" + card);
//            cardService.save(card);
//
//            cardList.add(card);
//            System.out.println("Added new card to cardList" + card);
//        }

//        setOfCards.setCards(cardList);

        save(setOfCards);

        return setOfCards;
    }

    public void save(SetOfCards setOfCards) {
        System.out.println("Set service: save set" + setOfCards);
        setOfCardsRepository.save(setOfCards);
    }

//    public void deleteById(String id) {
//        cardRepository.deleteById(id);
//    }

}
