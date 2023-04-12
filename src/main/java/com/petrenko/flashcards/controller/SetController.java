package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.FolderService;
import com.petrenko.flashcards.service.SetOfCardsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping()
public class SetController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SetController.class);

    private final CardService cardService;
    private final SetOfCardsService setOfCardsService;
    private final FolderService folderService;

    @Autowired
    public SetController(final CardService cardService,
                         final SetOfCardsService setOfCardsService,
                         final FolderService folderService) {
        this.cardService = cardService;
        this.setOfCardsService = setOfCardsService;
        this.folderService = folderService;
    }

    @GetMapping("/set/{id}")
    public ModelAndView getSetById(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/set/{id}) id: " + id);
        final SetOfCards setOfCards = setOfCardsService.getById(id);
        LOGGER.info("@GetMapping(/set/{id}) setOfCardsService.getById(id): " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        LOGGER.info("@GetMapping(/set/{id})  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");
        LOGGER.info("@GetMapping(/set/{id}) before show setById.html");
        return modelAndView;
    }

    @GetMapping("/set/create/{id}")  // with fill folder name by setId
    public ModelAndView getSetForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/set/create/{id}) with fill folder name");

        SetOfCards setOfCards = new SetOfCards();
        LOGGER.info("@GetMapping(/set/create/{id}) new setOfCards " + setOfCards);

        Folder folder = folderService.getById(id);
        LOGGER.info("@GetMapping(/set/create/{id}) folder getById" + folder);
        setOfCards.setFolder(folder);
        LOGGER.info("@GetMapping(/set/create/{id}) setOfCards with name of folder " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);

        modelAndView.setViewName("createSetView");
        LOGGER.info("@GetMapping(/set/create/{id}) before show createSetView");
        return modelAndView;
    }

    @GetMapping("/set/create")
    public ModelAndView getSetForm(ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/set/create)");
        SetOfCards setOfCards = new SetOfCards();
        LOGGER.info("@GetMapping(/set/create) new SetOfCards " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        modelAndView.setViewName("createSetView");
        LOGGER.info("@GetMapping(/set/create) before show createSetView.html");
        return modelAndView;
    }

    @PostMapping("/set/create")  // after created set
    public ModelAndView saveNewSet(@ModelAttribute SetOfCards setOfCards,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView) {
        LOGGER.info("@PostMapping(/set) " + setOfCards);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("setOfCards", setOfCards);
            modelAndView.setViewName("createSetView");
            return modelAndView;
        }

        folderService.save(setOfCards.getFolder());
        SetOfCards savedSetOfCards = setOfCardsService.save(setOfCards);
        LOGGER.info("@PostMapping(/set) savedSetOfCards " + savedSetOfCards);
        modelAndView.addObject("setOfCards", savedSetOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        LOGGER.info("@PostMapping(/set)  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");
        LOGGER.info("@PostMapping(/set) before show setById.html");
        return modelAndView;
    }


    @GetMapping("/set/{id}/edit")
    public ModelAndView getSetEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/set/{id}/edit) id: " + id);

        final SetOfCards setOfCards = setOfCardsService.getById(id);
        LOGGER.info("@GetMapping(/set/{id}/edit) setOfCardsService.getById(id): " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        LOGGER.info("@GetMapping(/set/{id}/edit)  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("editSetView");
        LOGGER.info("@GetMapping(/set/{id}/edit) before show editSetView.html");
        return modelAndView;
    }

    @PutMapping("/set/{id}/edit")  // after edited set
    public ModelAndView editSet(@PathVariable("id") String id,   // todo save id into Dto in view
                                @ModelAttribute SetOfCards setOfCards,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        LOGGER.info("@PutMapping(/set/{id}/edit) id: " + id);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("setOfCards", setOfCards);
            modelAndView.setViewName("editSetView");
            return modelAndView;
        }
        SetOfCards savedSetOfCards = setOfCardsService.editSetOfCards(setOfCards);
        LOGGER.info("@PutMapping(/set/{id}/edit) setOfCards saved " + savedSetOfCards);
        modelAndView.addObject("setOfCards", savedSetOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        LOGGER.info("@PutMapping(/set/{id}/edit)  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");
        LOGGER.info("@PutMapping(/set/{id}/edit) before show setById.html");
        return modelAndView;
    }

    @GetMapping("/set/{id}/delete")
    public ModelAndView getSetDeleteForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/set/{id}/delete) id: " + id);
        final SetOfCards setOfCards = setOfCardsService.getById(id);
        LOGGER.info("@GetMapping(/set/{id}/delete) setOfCards getById: " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        modelAndView.setViewName("deleteSetViewById");

        List<Card> cards = cardService.getBySet(setOfCards);
        LOGGER.info("@GetMapping(/set/{id})  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        LOGGER.info("@GetMapping(/set/{id}/delete) before show deleteSetViewById.html");
        return modelAndView;
    }

    @DeleteMapping("/set/{id}/delete")  // after delete card
    public ModelAndView deleteSet(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@DeleteMapping(/delete/{id}) id: " + id);

        Folder folder = setOfCardsService.getById(id).getFolder();
        LOGGER.info("@DeleteMapping(/set/{id}/delete) folder for setId: " + folder);
        modelAndView.addObject("folder", folder);

        setOfCardsService.deleteById(id);
        LOGGER.info("@DeleteMapping(/delete/{id}) setOfCards is deleted");

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        LOGGER.info("@GetMapping(/folder/{id}) List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderViewById");
        LOGGER.info("@GetMapping(/folder/{id}) before show folderViewById.html");

        return modelAndView;
    }


}
