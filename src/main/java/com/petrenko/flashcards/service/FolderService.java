package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.repository.CardRepository;
import com.petrenko.flashcards.repository.FolderRepository;
import com.petrenko.flashcards.repository.SetOfCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    private final SetOfCardsRepository setOfCardsRepository;

    @Autowired
    public FolderService(final FolderRepository folderRepository,
                         final SetOfCardsRepository setOfCardsRepository) {
        this.folderRepository = folderRepository;
        this.setOfCardsRepository = setOfCardsRepository;
    }

    public Folder save(Folder folder) {
        System.out.println("Set service: save set" + folder);
        return folderRepository.save(folder);
    }

    public Folder getById(String id) {
        Folder folder = folderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        System.out.println("Folder service: getById " + folder);
        return folder;
    }

    public Optional<Folder> getByName(String name) {
        return folderRepository.findByName(name);
    }
//
//    public void deleteById(String id) {
//        List<Card> cards = cardRepository.getBySetOfCards(getById(id));
//        cards.forEach(c -> cardRepository.deleteById(c.getId()));
//        setOfCardsRepository.deleteById(id);
//    }

}
