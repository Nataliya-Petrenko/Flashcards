package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final KeyWordService keyWordService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public CardService(final CardRepository cardRepository,
                       final KeyWordService keyWordService,
                       final SetOfCardsService setOfCardsService) {
        this.cardRepository = cardRepository;
        this.keyWordService = keyWordService;
        this.setOfCardsService = setOfCardsService;
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

    public String getPreviousCardIdById(String id) {
        Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        SetOfCards setOfCards = card.getSetOfCards();
        List<Card> cards = getBySet(setOfCards);
        int indexOfCard = cards.indexOf(card);
        int previousIndexOfCard = indexOfCard - 1;
//        int nextIndexOfCard = indexOfCard + 1;
        if (indexOfCard == 0) {
            previousIndexOfCard = cards.size() - 1;
        }
//        if (indexOfCard == cards.size() - 1) {
//            nextIndexOfCard = 0;
//        }

        String previousCardId = cards.get(previousIndexOfCard).getId();
//        String nextCard = cards.get(nextIndexOfCard).getId();

        return previousCardId;

//        Card card = cardRepository.getPreviousByCardId(id).orElseThrow(IllegalArgumentException::new);
//        return card.getId();
    }

    public Card saveToCard(CardCreatingDto cardCreatingDto) {
        Card card = new Card();
        card.setQuestion(cardCreatingDto.getQuestion());
        card.setShortAnswer(cardCreatingDto.getShortAnswer());
        card.setLongAnswer(cardCreatingDto.getLongAnswer());

        SetOfCards setOfCards = new SetOfCards();
        setOfCards.setName(cardCreatingDto.getSetOfCardsName());
        setOfCardsService.save(setOfCards);

        String keyWordsString = cardCreatingDto.getKeyWordsString();
        List<KeyWord> keyWords = keyWordService.stringToList(keyWordsString);
        card.setKeyWords(keyWords);

        card.setStudyPriority(StudyPriority.valueOf(cardCreatingDto.getStudyPriority()));
        card.setKnowledgeLevel(KnowledgeLevel.valueOf(cardCreatingDto.getKnowledgeLevel()));

        save(card);

        return card;
    }
}
