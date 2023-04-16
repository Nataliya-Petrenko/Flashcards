package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public CardService(final CardRepository cardRepository,
                       final SetOfCardsService setOfCardsService) {
        this.cardRepository = cardRepository;
        this.setOfCardsService = setOfCardsService;
    }

    public void deleteById(final String id) {
        LOGGER.info("invoked");
        cardRepository.deleteById(id);
    }

    public List<CardIdQuestionDto> getBySetId(final String setId) {
        LOGGER.info("invoked");
        List<CardIdQuestionDto> cardsIdQuestionDto = cardRepository.findBySetOfCardsId(setId);
        LOGGER.info("List<CardIdQuestionDto> {}", cardsIdQuestionDto);
        return cardsIdQuestionDto;
    }

    @Transactional
    public Card saveCardCreatingDtoToCard(final String userId, final CardCreatingDto cardCreatingDto) {
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

    public CardCreatingDto getCardCreatingDtoBySetId(final String setId) {
        LOGGER.info("invoked with setId {}", setId);

        SetNameFolderNameDto setNameFolderNameDto = setOfCardsService.getSetNameFolderNameDtoBySetId(setId);
        LOGGER.info("setNameFolderNameDto {}", setNameFolderNameDto);

        CardCreatingDto cardCreatingDto = new CardCreatingDto();

        cardCreatingDto.setSetOfCardsName(setNameFolderNameDto.getSetName());
        cardCreatingDto.setFolderName(setNameFolderNameDto.getFolderName());

        LOGGER.info("cardCreatingDto {}", cardCreatingDto);
        return cardCreatingDto;
    }

    public CardByIdDto getCardByIdDto(final String cardId) {
        LOGGER.info("invoked with cardId {}", cardId);

        CardByIdDto cardByIdDto = cardRepository.getCardByIdDto(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found for ID: " + cardId)); // todo error if empty set

        String setId = cardByIdDto.getSetOfCardsId();

        cardByIdDto.setPreviousOrLastCardId(getPreviousOrLastCardId(setId, cardId));// todo fix
        cardByIdDto.setNextOrFirstCardId(getNextOrFirstCardId(setId, cardId));// todo fix

        LOGGER.info("cardByIdDto {}", cardByIdDto);
        return cardByIdDto;
    }

    private String getPreviousOrLastCardId(final String setId, final String cardId) {
        LOGGER.info("invoked");
        String previousOrLastId = cardRepository.getPreviousId(setId, cardId)
                .orElse(cardRepository.getLastId(setId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find any previous or last card for ID: '" + cardId + "' in the set '" + setId + "'")));
        LOGGER.info("previousOrLastId {}", previousOrLastId);
        return previousOrLastId;
    }

    private String getNextOrFirstCardId(final String setId, final String cardId) {
        LOGGER.info("invoked");
        String nextOrFirstId = cardRepository.getNextId(setId, cardId)
                .orElse(cardRepository.getFirstId(setId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find any next or first card for ID: '" + cardId + "' in the set '" + setId + "'")));
        LOGGER.info("nextOrFirstId {}", nextOrFirstId);
        return nextOrFirstId;
    }

    public CardEditDto getCardEditDto(final String cardId) {
        LOGGER.info("invoked with cardId {}", cardId);

        CardEditDto cardEditDto = cardRepository.getCardEditDto(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card information for editing not found. Card ID: " + cardId));

        LOGGER.info("cardEditDto {}", cardEditDto);
        return cardEditDto;
    }

    @Transactional
    public Card updateCardByCardEditDto(final String userId, final CardEditDto cardEditDto) {
        LOGGER.info("invoked");

        String folderName = cardEditDto.getFolderName();
        String setName = cardEditDto.getSetOfCardsName();

        SetOfCards setOfCards = setOfCardsService.getByNameAndFolderNameOrNew(userId, folderName, setName);

        Card card = cardRepository.findById(cardEditDto.getId()).orElse(new Card());

        card.setQuestion(cardEditDto.getQuestion());
        card.setShortAnswer(cardEditDto.getShortAnswer());
        card.setLongAnswer(cardEditDto.getLongAnswer());
        card.setSetOfCards(setOfCards);

        Card savedCard = cardRepository.save(card);

        LOGGER.info("savedCard {}", savedCard);

        return savedCard;
    }

    public List<CardIdQuestionDto> getAll() {
        LOGGER.info("invoked");
        List<CardIdQuestionDto> cards = cardRepository.getAll();
        LOGGER.info("cards {}", cards);
        return cards;
    }

    public List<CardIdQuestionDto> getBySearch(final String search) { // todo: not dependents from case
        LOGGER.info("invoked");
        List<CardIdQuestionDto> cards = cardRepository.getBySearch(search);
        LOGGER.info("cards {}", cards);
        return cards;
    }
}
