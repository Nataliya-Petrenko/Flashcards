package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public CardCreatingDto getCardCreatingDtoBySetId(String setId) {
        LOGGER.info("invoked with setId {}", setId);

        SetNameFolderNameDto setNameFolderNameDto = setOfCardsService.getSetNameFolderNameDtoBySetId(setId);
        LOGGER.info("setNameFolderNameDto {}", setNameFolderNameDto);

        CardCreatingDto cardCreatingDto = new CardCreatingDto();

        cardCreatingDto.setSetOfCardsName(setNameFolderNameDto.getSetName());
        cardCreatingDto.setFolderName(setNameFolderNameDto.getFolderName());

        LOGGER.info("cardCreatingDto {}", cardCreatingDto);
        return cardCreatingDto;
    }

    public CardByIdDto getCardByIdDto(String cardId) { // todo get by single DTO
        LOGGER.info("invoked with cardId {}", cardId);

        CardByIdDto cardByIdDto = cardRepository.getCardByIdDto(cardId)
                .orElseThrow(IllegalArgumentException::new);

        String setId = cardByIdDto.getSetOfCardsId();

        cardByIdDto.setPreviousOrLastCardId(getPreviousOrLastCardId(setId, cardId));
        cardByIdDto.setNextOrFirstCardId(getNextOrFirstCardId(setId, cardId));

        LOGGER.info("cardByIdDto {}", cardByIdDto);
        return cardByIdDto;
    }

    private String getPreviousOrLastCardId(final String setId, final String cardId) {
        LOGGER.info("invoked");
        String previousOrLastId = cardRepository.getPreviousId(setId, cardId)
                .orElse(cardRepository.getLastId(setId)
                        .orElseThrow(IllegalArgumentException::new));
        LOGGER.info("previousOrLastId {}", previousOrLastId);
        return previousOrLastId;
    }

    private String getNextOrFirstCardId(final String setId, final String cardId) {
        LOGGER.info("invoked");
        String nextOrFirstId = cardRepository.getNextId(setId, cardId)
                .orElse(cardRepository.getFirstId(setId)
                        .orElseThrow(IllegalArgumentException::new));
        LOGGER.info("nextOrFirstId {}", nextOrFirstId);
        return nextOrFirstId;
    }

    public CardEditDto getCardEditDto(String cardId) {
        LOGGER.info("invoked with cardId {}", cardId);

        CardEditDto cardEditDto = cardRepository.getCardEditDto(cardId)
                .orElseThrow(IllegalArgumentException::new);

        LOGGER.info("cardEditDto {}", cardEditDto);
        return cardEditDto;
    }

    @Transactional
    public Card updateCardByCardEditDto(String userId, CardEditDto cardEditDto) {
        LOGGER.info("invoked");

        String folderName = cardEditDto.getFolderName();
        String setName = cardEditDto.getSetOfCardsName();

        SetOfCards setOfCards = setOfCardsService.getByNameAndFolderNameOrNew(userId, folderName, setName);

        Card card = cardRepository.findById(cardEditDto.getId()).orElse(new Card());

        card.setQuestion(cardEditDto.getQuestion());
        card.setShortAnswer(cardEditDto.getShortAnswer());
        card.setLongAnswer(cardEditDto.getLongAnswer());
        card.setSetOfCards(setOfCards);

        Card savedCard = cardRepository.save(card); // todo delete get folder after checking work

        LOGGER.info("savedCard {}", savedCard);

        return savedCard;
    }
}
