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

//    public CardViewByIdDto getCardViewByIdDto(final String id) {  // query doesn't work
//        CardViewByIdDto cardViewByIdDto = cardRepository.cardViewByIdDto(id).orElseThrow(IllegalArgumentException::new);
//        System.out.println("CardService: cardViewByIdDto " + cardViewByIdDto);
//        return cardViewByIdDto;
//    }

    public Card newCard(final SetOfCards setOfCards) {
        Card card = new Card();
        card.setSetOfCards(setOfCards);
        card.setQuestion("question");
        save(card);
        System.out.println("Service: Created card " + card);
        return card;
    }

    public Card save(final Card card) {
        Card savedCard = cardRepository.save(card);
        System.out.println("Service: Saved card " + card.getId());
        return savedCard;
    }

    public List<Card> getBySet(final SetOfCards setOfCards) {
        List<Card> bySetOfCards = cardRepository.getBySetOfCards(setOfCards);
        System.out.println("Service: getBySet: " + bySetOfCards);
        return bySetOfCards;
    }

    public Card saveToCard(final CardCreatingDto cardCreatingDto) {
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

        return save(card);
    }

    public String getNextOrFirstCardId(final String id) {
        return cardRepository.getNextCardId(id)
                .orElse(cardRepository.getFirstCardId()
                        .orElseThrow(IllegalArgumentException::new));
    }

    public String getPreviousOrLastCardId(final String id) {
        return cardRepository.getPreviousCardId(id)
                .orElse(cardRepository.getLastCardId()
                        .orElseThrow(IllegalArgumentException::new));
    }

    public CardEditingDto getCardEditingDto(final String id) {
        return cardRepository.getCardEditingDto(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Card editCardByCardEditingDto(final CardEditingDto cardEditingDto) {
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

        Card savedCard = save(card);
        System.out.println("Service editCardByCardEditingDto: card after checking" + card);
        return savedCard;
    }

    public void deleteById(final String  id) {
        cardRepository.deleteById(id);
    }
}
