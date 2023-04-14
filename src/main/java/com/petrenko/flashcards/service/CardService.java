package com.petrenko.flashcards.service;

import com.petrenko.flashcards.controller.CardController;
import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.dto.CardEditingDto;
import com.petrenko.flashcards.dto.CardIdQuestionDto;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final SetOfCardsService setOfCardsService;
    private final FolderService folderService;

    @Autowired
    public CardService(final CardRepository cardRepository,
                       final SetOfCardsService setOfCardsService,
                       final FolderService folderService) {
        this.cardRepository = cardRepository;
        this.setOfCardsService = setOfCardsService;
        this.folderService = folderService;
    }

    public Card getById(final String id) {
        Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        LOGGER.info("Service: getById: " + card);
        return card;
    }

//    public CardViewByIdDto getCardViewByIdDto(final String id) {  // query doesn't work
//        CardViewByIdDto cardViewByIdDto = cardRepository.cardViewByIdDto(id).orElseThrow(IllegalArgumentException::new);
//        LOGGER.info("CardService: cardViewByIdDto " + cardViewByIdDto);
//        return cardViewByIdDto;
//    }

//    public Card newCard(final SetOfCards setOfCards) {
//        Card card = new Card();
//        card.setSetOfCards(setOfCards);
//        card.setQuestion("question");
//        save(card);
//        LOGGER.info("Service: Created card " + card);
//        return card;
//    }

//    public Card createNewEmptyCard(){
//        Folder folder = new Folder();
//        SetOfCards setOfCards = new SetOfCards();
//        setOfCards.setFolder(folder);
//        Card card = new Card();
//        card.setSetOfCards(setOfCards);
//        return card;
//    }

    public Card save(final String userId, final Card card) {

        SetOfCards setOfCards = setOfCardsService.saveCheckName(userId, card.getSetOfCards());
        card.setSetOfCards(setOfCards);
        card.setTimeOfCreation(LocalDateTime.now());
        Card savedCard = cardRepository.save(card);

        LOGGER.info("Service: Saved card " + savedCard);
        return savedCard;
    }

    public List<Card> getBySet(final SetOfCards setOfCards) {
        List<Card> bySetOfCards = cardRepository.getBySetOfCards(setOfCards);
        LOGGER.info("List<Card> bySetOfCards: {}", bySetOfCards);
        return bySetOfCards;
    }

//    public Card saveToCard(final CardCreatingDto cardCreatingDto) {
//        Card card = new Card();
//        card.setQuestion(cardCreatingDto.getQuestion());
//        card.setShortAnswer(cardCreatingDto.getShortAnswer());
//        card.setLongAnswer(cardCreatingDto.getLongAnswer());
//
//        final String setOfCardsName = cardCreatingDto.getSetOfCardsName();
//        setOfCardsService.getByName(setOfCardsName).ifPresentOrElse(card::setSetOfCards,   //  todo how to break cyclical dependence:  cardService <--> setOfCardsService?
//                () -> {      //todo if a set with this name in this folder exist then show a massage and suggest the choice to set Set from rep or create a new one with another name
//                    SetOfCards setOfCards = new SetOfCards();
//                    setOfCards.setName(setOfCardsName);
//                    setOfCardsService.save(setOfCards);
//                    card.setSetOfCards(setOfCards);
//                });
//
//        return save(card);
//    }

    public String getNextOrFirstCardId(final String userId, final String cardId) { // todo add dependency on set and folder
        LOGGER.info("invoked");
        String nextOrFirstCardId = cardRepository.getNextCardId(cardId)
                .orElse(cardRepository.getFirstCardId(cardId)
                        .orElseThrow(IllegalArgumentException::new));
//        String nextOrFirstCardId = cardRepository.getNextCardId(userId, cardId)
//                .orElse(cardRepository.getFirstCardId(userId, cardId)
//                        .orElseThrow(IllegalArgumentException::new));
        LOGGER.info("nextOrFirstCardId " + nextOrFirstCardId);
        return nextOrFirstCardId;
    }

    public String getPreviousOrLastCardId(final String userId, final String cardId) { // todo add dependency on set and folder
        LOGGER.info("invoked");
        String previousOrLastCardId = cardRepository.getPreviousCardId(cardId)
                .orElse(cardRepository.getLastCardId(cardId)
                        .orElseThrow(IllegalArgumentException::new));
//        String previousOrLastCardId = cardRepository.getPreviousCardId(userId, cardId)
//                .orElse(cardRepository.getLastCardId(userId, cardId)
//                        .orElseThrow(IllegalArgumentException::new));
        LOGGER.info("previousOrLastCardId {}", previousOrLastCardId);

//        Optional<String> previousCardIdNew = cardRepository.getPreviousCardIdNew(userId, cardId);
//        LOGGER.info("getPreviousCardIdNew {}", previousCardIdNew.get());
        return previousOrLastCardId;
    }

    public CardEditingDto getCardEditingDto(final String id) {
        return cardRepository.getCardEditingDto(id)
                .orElseThrow(IllegalArgumentException::new);
    }

//    public Card editCardByCardEditingDto(final CardEditingDto cardEditingDto) {
//        LOGGER.info("Service editCardByCardEditingDto: start " + cardEditingDto);
//        Card card = cardRepository.findById(cardEditingDto.getId()).orElseThrow(IllegalArgumentException::new);
//        LOGGER.info("Service editCardByCardEditingDto: card from rep" + card);
//        card.setQuestion(cardEditingDto.getQuestion());
//        card.setShortAnswer(cardEditingDto.getShortAnswer());
//        card.setLongAnswer(cardEditingDto.getLongAnswer());
//
//        final String newSetOfCardsName = cardEditingDto.getSetOfCardsName();
//        LOGGER.info("Service editCardByCardEditingDto: newSetOfCardsName" + newSetOfCardsName);
//
//        setOfCardsService.getByName(newSetOfCardsName).ifPresentOrElse(card::setSetOfCards,
//                () -> {  //todo if a set with this name in this folder exist then show a massage and suggest the choice to set Set from rep or create a new one with another name
//                    SetOfCards setOfCards = new SetOfCards();
//                    setOfCards.setName(newSetOfCardsName);
//                    setOfCardsService.save(setOfCards);
//                    card.setSetOfCards(setOfCards);
//                });
//
//        Card savedCard = save(card);
//        LOGGER.info("Service editCardByCardEditingDto: card after checking" + card);
//        return savedCard;
//    }

    public void deleteById(final String id) {
        LOGGER.info("invoked");
        cardRepository.deleteById(id);
    }

    public List<CardIdQuestionDto> getBySetId(String setId) {
        LOGGER.info("invoked");
        List<CardIdQuestionDto> cardsIdQuestionDto = cardRepository.findBySetOfCardsId(setId);
        LOGGER.info("List<CardIdQuestionDto> {}", cardsIdQuestionDto);
        return cardsIdQuestionDto;
    }

    @Transactional
    public Card saveCardCreatingDtoToCard(String userId, CardCreatingDto cardCreatingDto) {
        LOGGER.info("invoked");

        String folderName = cardCreatingDto.getFolderName();
        String setName = cardCreatingDto.getSetOfCardsName();

        SetOfCards setOfCards = setOfCardsService.getByNameAndFolderNameOrNew(userId, folderName, setName);

        String question = cardCreatingDto.getQuestion();
        String shortAnswer = cardCreatingDto.getShortAnswer();
        String longAnswer = cardCreatingDto.getLongAnswer();

        Card card = new Card();
        card.setQuestion(question);
        card.setShortAnswer(shortAnswer);
        card.setLongAnswer(longAnswer);
        card.setSetOfCards(setOfCards);

        Card savedCard = cardRepository.save(card);

        LOGGER.info("savedCard {}", savedCard);
        return savedCard;
    }
}
