package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.SetOfCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SetOfCardsService {

    private final SetOfCardsRepository setOfCardsRepository;
    //    private final CardService cardService;
    private final CardRepository cardRepository;

    private final FolderService folderService;

    @Autowired
    public SetOfCardsService(final SetOfCardsRepository setOfCardsRepository,
//                             final CardService cardService,
                             final CardRepository cardRepository,
                             final FolderService folderService) {
        this.setOfCardsRepository = setOfCardsRepository;
//        this.cardService = cardService;
        this.cardRepository = cardRepository;
        this.folderService = folderService;
    }

    public SetOfCards save(SetOfCards setOfCards) {
        System.out.println("Set service: save set" + setOfCards);
        return setOfCardsRepository.save(setOfCards);
    }

    public SetOfCards getById(String id) {
        SetOfCards setOfCards = setOfCardsRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        System.out.println("Set service: getById: " + setOfCards);
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
        System.out.println("SetOfCards Service: getByFolder: " + byFolder);
        return byFolder;
    }

    public SetOfCards editSetOfCards(final SetOfCards setOfCards) {
        System.out.println("Set service editSetOfCards " + setOfCards);

        final String newFolderName = setOfCards.getFolder().getName();
        System.out.println("Set service editSetOfCards: newFolderName" + newFolderName);

        folderService.getByName(newFolderName).ifPresentOrElse((setOfCards::setFolder),
                () -> {
                    Folder folder = new Folder();
                    folder.setName(newFolderName);
                    folderService.save(folder);
                    setOfCards.setFolder(folder);
                });

        SetOfCards savedSetOfCards = save(setOfCards);
        System.out.println("Set service editSetOfCards: savedSetOfCards" + savedSetOfCards);
        return savedSetOfCards;
    }

}
