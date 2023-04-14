package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.FolderRepository;
import com.petrenko.flashcards.repository.SetOfCardsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SetOfCardsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SetOfCardsService.class);
    private final SetOfCardsRepository setOfCardsRepository;
    private final CardRepository cardRepository;

    private final FolderService folderService;

    private final FolderRepository folderRepository;

    @Autowired
    public SetOfCardsService(final SetOfCardsRepository setOfCardsRepository,
                             final CardRepository cardRepository,
                             final FolderService folderService,
                             final FolderRepository folderRepository) {
        this.setOfCardsRepository = setOfCardsRepository;
        this.cardRepository = cardRepository;
        this.folderService = folderService;
        this.folderRepository = folderRepository;
    }

    public SetOfCards saveCheckName(String userId, SetOfCards setOfCards) {
        Folder folder = folderService.saveCheckName(userId, setOfCards.getFolder());

        String setName = setOfCards.getName();
        final SetOfCards finalSet = setOfCardsRepository.findByName(setOfCards.getName()).orElse(new SetOfCards());
        finalSet.setName(setName);
        finalSet.setFolder(folder);
        finalSet.setTimeOfCreation(LocalDateTime.now());

        SetOfCards savedSet = setOfCardsRepository.save(finalSet);

        LOGGER.info("Set service: save set (check name)" + savedSet);
        return savedSet;
    }

    public SetOfCards save(SetOfCards setOfCards) {     //todo if a folder with this name exist then show a massage and suggest the choice to set folder from rep or create a new one with another name
        LOGGER.info("Set service: save set" + setOfCards);
        return setOfCardsRepository.save(setOfCards);
    }

    public SetOfCards getById(String id) {
        SetOfCards setOfCards = setOfCardsRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        LOGGER.info("Set service: getById: " + setOfCards);
        return setOfCards;
    }

    public Optional<SetOfCards> getByName(String name) {
        return setOfCardsRepository.findByName(name);
    }

    public void deleteById(String id) {
        List<Card> cards = cardRepository.getBySetOfCards(getById(id));
        cards.forEach(c -> cardRepository.deleteById(c.getId()));
        setOfCardsRepository.deleteById(id);
    }

    public List<SetOfCards> getByFolder(final Folder folder) {
        List<SetOfCards> byFolder = setOfCardsRepository.getByFolder(folder);
        LOGGER.info("SetOfCards Service: getByFolder: " + byFolder);
        return byFolder;
    }

    public SetOfCards editSetOfCards(final SetOfCards setOfCards) {
        LOGGER.info("{}", setOfCards);

        final String newFolderName = setOfCards.getFolder().getName();
        LOGGER.info("newFolderName {}", newFolderName);

        folderService.getByName(newFolderName).ifPresentOrElse((setOfCards::setFolder),      //todo if a folder with this name exist then show a massage and suggest the choice to set folder from rep or create a new one with another name
                () -> {
                    Folder folder = new Folder();
                    folder.setName(newFolderName);
                    folderService.save(folder);
                    setOfCards.setFolder(folder);
                });

        SetOfCards savedSetOfCards = save(setOfCards);
        LOGGER.info("savedSetOfCards {}", savedSetOfCards);
        return savedSetOfCards;
    }

//    public Optional<String> getIdFromFolderByName(String name, String folderId) {
//        Optional<String> idFromFolderByName = getIdFromFolderByName(name, folderId);
//        LOGGER.info("get SetOfCards FromFolderByName " + idFromFolderByName.get());
//        return idFromFolderByName;
//    }

    // new
    public List<SetIdNameDto> getByFolderId(String folderId) {
        LOGGER.info("invoked");
        List<SetIdNameDto> sets = setOfCardsRepository.getByFolderId(folderId);
        LOGGER.info("SetOfCards getByFolderId: {}", sets);
        return sets;
    }

    public SetOfCards saveSetFolderNameSetNameDescriptionDto(String userId, SetFolderNameSetNameDescriptionDto setDto) {
        LOGGER.info("invoked");

        String newFolderName = setDto.getFolderName();
        String newSetName = setDto.getName();

        SetOfCards setOfCards = getByNameAndFolderNameOrNew(userId, newFolderName, newSetName);

        setOfCards.setDescription(setDto.getDescription());

        SetOfCards savedSet = setOfCardsRepository.save(setOfCards);

        LOGGER.info("savedSet {}", savedSet);
        return savedSet;
    }

    public SetOfCards getByNameAndFolderNameOrNew(String userId, String folderName, String setName) {
        LOGGER.info("invoked");

        Folder folder = folderService.getFolderWithNameOrNew(userId, folderName);

        final SetOfCards setOfCards = setOfCardsRepository.findByNameAndFolderId(setName, folder.getId())
                .orElseGet(() -> {
                    SetOfCards newSet = new SetOfCards();
                    newSet.setName(setName);
                    newSet.setFolder(folder);
                    return setOfCardsRepository.save(newSet);
                });

        LOGGER.info("setOfCards {}", setOfCards);
        return setOfCards;
    }

    @Transactional
    public SetViewByIdDto getSetViewByIdDto(String setId) { // todo get by single DTO
        LOGGER.info("invoked");

        SetViewByIdDto setViewByIdDto = setOfCardsRepository
                .getSetViewByIdDto(setId).orElseThrow(IllegalArgumentException::new);

        String folderId = setViewByIdDto.getFolderId();

        setViewByIdDto.setPreviousOrLastSetId(getPreviousOrLastSetId(folderId, setId));
        setViewByIdDto.setNextOrFirstSetId(getNextOrFirstSetId(folderId, setId));

        LOGGER.info("setViewByIdDto {}", setViewByIdDto);
        return setViewByIdDto;
    }

    private String getPreviousOrLastSetId(final String folderId, final String setId) {
        LOGGER.info("invoked");
        String previousOrLastSetId = setOfCardsRepository.getPreviousId(folderId, setId)
                .orElse(setOfCardsRepository.getLastId(folderId)
                        .orElseThrow(IllegalArgumentException::new));
        LOGGER.info("previousOrLastSetId {}", previousOrLastSetId);
        return previousOrLastSetId;
    }

    private String getNextOrFirstSetId(final String folderId, final String setId) {
        LOGGER.info("invoked");
        String nextOrFirstSetId = setOfCardsRepository.getNextId(folderId, setId)
                .orElse(setOfCardsRepository.getFirstId(folderId)
                        .orElseThrow(IllegalArgumentException::new));
        LOGGER.info("nextOrFirstFolderId {}", nextOrFirstSetId);
        return nextOrFirstSetId;
    }

    public SetEditDto getSetEditDto(String setId) {
        LOGGER.info("invoked");

        SetEditDto setEditDto = setOfCardsRepository
                .getSetEditDto(setId).orElseThrow(IllegalArgumentException::new);

        LOGGER.info("setEditDto {}", setEditDto);
        return setEditDto;
    }

    @Transactional
    public SetOfCards updateSetOfCardsBySetEditDto(String userId, SetEditDto setEditDto) {
        LOGGER.info("invoked");

        String folderName = setEditDto.getFolderName();
        String setName = setEditDto.getName();

        SetOfCards setOfCards = getByNameAndFolderNameOrNew(userId, folderName, setName);

        String description = setEditDto.getDescription();
        setOfCards.setDescription(description);

        String setId = setOfCards.getId();

        setOfCardsRepository.updateDescription(setId, description);

        SetOfCards updatedSetOfCards = setOfCardsRepository.findById(setId)
                .orElseThrow(IllegalArgumentException::new);      // todo delete get folder after checking work

        LOGGER.info("updatedSetOfCards {}", updatedSetOfCards);
        return updatedSetOfCards;
    }
}
