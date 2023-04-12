package com.petrenko.flashcards.service;

import com.petrenko.flashcards.controller.CardController;
import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.SetOfCardsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SetOfCardsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SetOfCardsService.class);
    private final SetOfCardsRepository setOfCardsRepository;
    private final CardRepository cardRepository;

    private final FolderService folderService;

    @Autowired
    public SetOfCardsService(final SetOfCardsRepository setOfCardsRepository,
                             final CardRepository cardRepository,
                             final FolderService folderService
    ) {
        this.setOfCardsRepository = setOfCardsRepository;
        this.cardRepository = cardRepository;
        this.folderService = folderService;
    }

    public SetOfCards saveCheckName(SetOfCards setOfCards) {
        Folder folder = folderService.saveCheckName(setOfCards.getFolder());

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
        LOGGER.info("Set service editSetOfCards " + setOfCards);

        final String newFolderName = setOfCards.getFolder().getName();
        LOGGER.info("Set service editSetOfCards: newFolderName" + newFolderName);

        folderService.getByName(newFolderName).ifPresentOrElse((setOfCards::setFolder),      //todo if a folder with this name exist then show a massage and suggest the choice to set folder from rep or create a new one with another name
                () -> {
                    Folder folder = new Folder();
                    folder.setName(newFolderName);
                    folderService.save(folder);
                    setOfCards.setFolder(folder);
                });

        SetOfCards savedSetOfCards = save(setOfCards);
        LOGGER.info("Set service editSetOfCards: savedSetOfCards" + savedSetOfCards);
        return savedSetOfCards;
    }

    public Optional<String> getIdFromFolderByName(String name, String folderId) {
        Optional<String> idFromFolderByName = getIdFromFolderByName(name, folderId);
        LOGGER.info("get SetOfCards FromFolderByName " + idFromFolderByName.get());
        return idFromFolderByName;
    }
}
