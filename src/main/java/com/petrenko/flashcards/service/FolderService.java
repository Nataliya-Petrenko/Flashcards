package com.petrenko.flashcards.service;

import com.petrenko.flashcards.controller.CardController;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FolderService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FolderService.class);
    private final FolderRepository folderRepository;
    private final SetOfCardsRepository setOfCardsRepository;
    private final CardRepository cardRepository;

    @Autowired
    public FolderService(final FolderRepository folderRepository,
                         final SetOfCardsRepository setOfCardsRepository,
                         final CardRepository cardRepository) {
        this.folderRepository = folderRepository;
        this.setOfCardsRepository = setOfCardsRepository;
        this.cardRepository = cardRepository;
    }



    public Folder saveCheckName(Folder folder) { //todo if a folder with this name exist then show a massage and suggest the choice to update the old one or create a new one with another name
        String folderName = folder.getName();
        final Folder finalFolder = folderRepository.findByName(folder.getName()).orElse(new Folder());
        finalFolder.setName(folderName);
        finalFolder.setTimeOfCreation(LocalDateTime.now());
        Folder savedFolder = folderRepository.save(finalFolder);
        LOGGER.info("Folder service: save folder (check name)" + savedFolder);
        return folderRepository.save(savedFolder);
    }

    public Folder save(Folder folder) { //todo if a folder with this name exist then show a massage and suggest the choice to update the old one or create a new one with another name
        LOGGER.info("Set service: save set" + folder);
        return folderRepository.save(folder);
    }

    public Folder getById(String id) {
        Folder folder = folderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        LOGGER.info("Folder service: getById " + folder);
        return folder;
    }

    public Optional<Folder> getByName(String name) {
        return folderRepository.findByName(name);
    }


    public void deleteById(String id) {
        List<SetOfCards> sets = setOfCardsRepository.getByFolder(getById(id));
        sets.forEach(s -> {                       // todo remove repeating from setService (with break cyclical dependence)
            List<Card> cards = cardRepository.getBySetOfCards(s);
            cards.forEach(c -> cardRepository.deleteById(c.getId()));
            setOfCardsRepository.deleteById(id);
        });
        folderRepository.deleteById(id);
    }

}
