package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.dto.CardEditingDto;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public CardService(final CardRepository cardRepository,
                       final SetOfCardsService setOfCardsService) {
        this.cardRepository = cardRepository;
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

    public Card saveToCard(CardCreatingDto cardCreatingDto) {
        Card card = new Card();
        card.setQuestion(cardCreatingDto.getQuestion());
        card.setShortAnswer(cardCreatingDto.getShortAnswer());
        card.setLongAnswer(cardCreatingDto.getLongAnswer());

        final String setOfCardsName = cardCreatingDto.getSetOfCardsName();
        setOfCardsService.getByName(setOfCardsName).ifPresentOrElse(card::setSetOfCards,
                () -> {
                    SetOfCards setOfCards = new SetOfCards();
                    setOfCards.setName(setOfCardsName);
                    setOfCardsService.save(setOfCards);
                    card.setSetOfCards(setOfCards);
                });

        save(card);

        return card;
    }

    public String getNextOrFirstCardId(String id) {
        return cardRepository.getNextCardId(id)
                .orElse(cardRepository.getFirstCardId()
                        .orElseThrow(IllegalArgumentException::new));
    }

    public String getPreviousOrLastCardId(String id) {
        return cardRepository.getPreviousCardId(id)
                .orElse(cardRepository.getLastCardId()
                        .orElseThrow(IllegalArgumentException::new));
    }

    public CardEditingDto getCardEditingDto(String id) {
        return cardRepository.getCardEditingDto(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Card editCardByCardEditingDto(CardEditingDto cardEditingDto) {
        System.out.println("Service editCardByCardEditingDto: start " + cardEditingDto);
        Card card = cardRepository.findById(cardEditingDto.getId()).orElseThrow(IllegalArgumentException::new);
        System.out.println("Service editCardByCardEditingDto: card from rep" + card);
        card.setQuestion(cardEditingDto.getQuestion());
        card.setShortAnswer(cardEditingDto.getShortAnswer());
        card.setLongAnswer(cardEditingDto.getLongAnswer());

        final String newSetOfCardsName = cardEditingDto.getSetOfCardsName();
        System.out.println("Service editCardByCardEditingDto: newSetOfCardsName" + newSetOfCardsName);

        setOfCardsService.getByName(newSetOfCardsName).ifPresentOrElse(card::setSetOfCards,
                () -> {
                    SetOfCards setOfCards = new SetOfCards();
                    setOfCards.setName(newSetOfCardsName);
                    setOfCardsService.save(setOfCards);
                    card.setSetOfCards(setOfCards);
                });

// todo if match with another name set that set another set for card

        save(card);
        System.out.println("Service editCardByCardEditingDto: card after checking" + card);
        return card;
    }




    public void deleteById(String  id) {
        cardRepository.deleteById(id);
    }
}
