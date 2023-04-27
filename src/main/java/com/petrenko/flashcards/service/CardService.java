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
import java.util.Optional;

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
        cardRepository.deleteById(id);
        LOGGER.info("card with id: '{}' deleted", id);
    }

    public List<CardIdQuestionDto> getBySetId(final String setId) {
        final List<CardIdQuestionDto> cardsIdQuestionDto = cardRepository.findBySetOfCardsId(setId);
        LOGGER.info("List<CardIdQuestionDto> for setId '{}': {}", setId, cardsIdQuestionDto);
        return cardsIdQuestionDto;
    }

    @Transactional
    public Card saveCardCreatingDtoToCard(final String userId, final CardCreatingDto cardCreatingDto) {
        final String folderName = cardCreatingDto.getFolderName();
        final String setName = cardCreatingDto.getSetOfCardsName();

        final SetOfCards setOfCards = setOfCardsService.getByNameAndFolderNameOrNew(userId, folderName, setName);

        final String question = cardCreatingDto.getQuestion();
        final String shortAnswer = cardCreatingDto.getShortAnswer();
        final String longAnswer = cardCreatingDto.getLongAnswer();

        Card card = new Card();
        card.setQuestion(question);
        card.setShortAnswer(shortAnswer);
        card.setLongAnswer(longAnswer);
        card.setSetOfCards(setOfCards);

        final Card savedCard = cardRepository.save(card);

        LOGGER.info("For userId '{}' and CardCreatingDto '{}' was savedCard '{}'", userId, cardCreatingDto, savedCard);
        return savedCard;
    }

    public CardCreatingDto getCardCreatingDtoBySetId(final String setId) {
        LOGGER.info("invoked with setId {}", setId);

        final SetNameFolderNameDto setNameFolderNameDto = setOfCardsService.getSetNameFolderNameDtoBySetId(setId);
        LOGGER.info("setNameFolderNameDto {}", setNameFolderNameDto);

        final CardCreatingDto cardCreatingDto = new CardCreatingDto();

        cardCreatingDto.setSetOfCardsName(setNameFolderNameDto.getSetName());
        cardCreatingDto.setFolderName(setNameFolderNameDto.getFolderName());

        LOGGER.info("cardCreatingDto {}", cardCreatingDto);
        return cardCreatingDto;
    }

    public CardByIdDto getCardByIdDto(final String cardId) {
        LOGGER.info("invoked with cardId {}", cardId);

        final CardByIdDto cardByIdDto = cardRepository.getCardByIdDto(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found for ID: " + cardId));

        final String setId = cardByIdDto.getSetOfCardsId();

        cardByIdDto.setPreviousOrLastCardId(getPreviousOrLastCardId(setId, cardId));
        cardByIdDto.setNextOrFirstCardId(getNextOrFirstCardId(setId, cardId));

        LOGGER.info("cardByIdDto {}", cardByIdDto);
        return cardByIdDto;
    }

    private String getPreviousOrLastCardId(final String setId, final String cardId) {
        LOGGER.info("invoked with setId '{}' and cardId '{}'", setId, cardId);
        final String previousOrLastId = cardRepository.getPreviousId(setId, cardId)
                .orElse(cardRepository.getLastId(setId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find any previous or last card " +
                                "for ID: '" + cardId + "' in the set '" + setId + "'")));
        LOGGER.info("previousOrLastId {}", previousOrLastId);
        return previousOrLastId;
    }

    private String getNextOrFirstCardId(final String setId, final String cardId) {
        LOGGER.info("invoked with setId '{}' and cardId '{}'", setId, cardId);
        final String nextOrFirstId = cardRepository.getNextId(setId, cardId)
                .orElse(cardRepository.getFirstId(setId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find any next or first card " +
                                "for ID: '" + cardId + "' in the set '" + setId + "'")));
        LOGGER.info("nextOrFirstId {}", nextOrFirstId);
        return nextOrFirstId;
    }

    public CardEditDto getCardEditDto(final String cardId) {
        LOGGER.info("invoked with cardId {}", cardId);

        final CardEditDto cardEditDto = cardRepository.getCardEditDto(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card information for editing not found. Card ID: "
                                                                + cardId));

        LOGGER.info("cardEditDto {}", cardEditDto);
        return cardEditDto;
    }

    @Transactional
    public Card updateCardByCardEditDto(final String userId, final CardEditDto cardEditDto) {
        LOGGER.info("invoked with userId '{}' and cardEditDto '{}'", userId, cardEditDto);

        final String folderName = cardEditDto.getFolderName();
        final String setName = cardEditDto.getSetOfCardsName();

        final SetOfCards setOfCards = setOfCardsService.getByNameAndFolderNameOrNew(userId, folderName, setName);

        final Card card = cardRepository.findById(cardEditDto.getId()).orElse(new Card());

        card.setQuestion(cardEditDto.getQuestion());
        card.setShortAnswer(cardEditDto.getShortAnswer());
        card.setLongAnswer(cardEditDto.getLongAnswer());
        card.setSetOfCards(setOfCards);

        final Card savedCard = cardRepository.save(card);
        LOGGER.info("savedCard {}", savedCard);

        return savedCard;
    }

    public List<CardIdQuestionDto> getAll() {
        final List<CardIdQuestionDto> cards = cardRepository.getAll();
        LOGGER.info("cards {}", cards);
        return cards;
    }

    public List<CardIdQuestionDto> getBySearch(final String search) {
        final List<CardIdQuestionDto> cards = cardRepository.getBySearch(search);
        LOGGER.info("for search '{}': cards {}", search, cards);
        return cards;
    }

    public Optional<String> getFirstId(String setId) {
        LOGGER.info("invoked for setId '{}'", setId);
        return cardRepository.getFirstId(setId);
    }

    public void deleteBySetId(String setId) {
        cardRepository.deleteBySetId(setId);
        LOGGER.info("deleted cards with setId '{}'", setId);
    }
}
