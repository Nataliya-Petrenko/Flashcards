package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
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
public class CardController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CardController.class);
    private final CardService cardService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public CardController(final CardService cardService,
                          final SetOfCardsService setOfCardsService) {
        this.cardService = cardService;
        this.setOfCardsService = setOfCardsService;
    }
//    @GetMapping("/test")
//    public ModelAndView getTest(ModelAndView modelAndView) {
////        modelAndView.setViewName("admin-view");
////        modelAndView.setViewName("all-users-info-view");
//        modelAndView.setViewName("card-view");
////        modelAndView.setViewName("complaint-view");
////        modelAndView.setViewName("edit-card-view");
////        modelAndView.setViewName("edit-folder-view");
////        modelAndView.setViewName("edit-profile-view");
////        modelAndView.setViewName("edit-set-view");
////        modelAndView.setViewName("error-view");
////        modelAndView.setViewName("folder-view");
////        modelAndView.setViewName("help-view");
////        modelAndView.setViewName("index");
////        modelAndView.setViewName("learning-view");
////        modelAndView.setViewName("profile-view");
////        modelAndView.setViewName("set-view");
////        modelAndView.setViewName("sign-in-view");
////        modelAndView.setViewName("sign-up-view");
////        modelAndView.setViewName("statistics-view");
//        return modelAndView;
//    }

    @GetMapping("/card/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id, ModelAndView modelAndView) {
        final String previousCardId = cardService.getPreviousOrLastCardId(id);
        LOGGER.info("previousCardId " + previousCardId);
        modelAndView.addObject("previousCardId", previousCardId);

        final String nextCardId = cardService.getNextOrFirstCardId(id);
        LOGGER.info("nextCardId " + nextCardId);
        modelAndView.addObject("nextCardId", nextCardId);

        final Card card = cardService.getById(id);
        modelAndView.addObject("card", card);
        LOGGER.info("Card by id: " + card);

        modelAndView.setViewName("cardById");
        return modelAndView;
    }

    @GetMapping("/card/create/{id}")  // with fill set name by setId
    public ModelAndView getCreateCardFormWithSet(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("invoked");

        Card card = new Card();
        LOGGER.info("new Card() " + card);

        SetOfCards setOfCards = setOfCardsService.getById(id);
        LOGGER.info("setOfCards getById" + setOfCards);
        card.setSetOfCards(setOfCards);
        LOGGER.info("card with name of set " + card);
        modelAndView.addObject("card", card);

        modelAndView.setViewName("cardCreate");
        LOGGER.info("before show cardCreate");
        return modelAndView;
    }

    @GetMapping("/card/create")
    public ModelAndView getCreateCardForm(ModelAndView modelAndView) {
        LOGGER.info("invoked");

        Card card = new Card();
        LOGGER.info("new Card() " + card);
        modelAndView.addObject("card", card);

        modelAndView.setViewName("cardCreate");
        LOGGER.info("before show cardCreate");
        return modelAndView;  // todo changed saving card to saving set and folder correctly but template doesn't parse
    }

    @PostMapping("/card/create")  // after created card
    public ModelAndView createCard(@ModelAttribute Card card,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView) {
        LOGGER.info("card from form " + card);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("card", card);
            modelAndView.setViewName("cardCreate");
            return modelAndView;
        }
        Card savedCard = cardService.save(card);
        LOGGER.info("card saved " + savedCard);

        final SetOfCards setOfCards = card.getSetOfCards();
        LOGGER.info("setOfCards: " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);

        List<Card> cards = cardService.getBySet(card.getSetOfCards());
        LOGGER.info("List<Card>: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");
        LOGGER.info("before show setById.html");
        return modelAndView;
    }

    @GetMapping("/card/{id}/edit")
    public ModelAndView getCardEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        final Card card = cardService.getById(id);
        LOGGER.info("card getById: " + card);
        modelAndView.addObject("card", card);
        modelAndView.setViewName("cardEdit");
        LOGGER.info("before show cardEdit.html");
        return modelAndView;
    }

    @PutMapping("/card/{id}/edit")  // after edited card
    public ModelAndView editCard(@PathVariable("id") String id,   // todo save id into cardEditingDto in view
                                 @ModelAttribute Card card,
                                 BindingResult bindingResult,
                                 ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("card", card);
            modelAndView.setViewName("cardEdit");
            return modelAndView;
        }
        LOGGER.info("card from form " + card);

        Card savedCard = cardService.save(card);
        LOGGER.info("card saved " + savedCard);
        modelAndView.addObject("card", savedCard);

        final String previousCardId = cardService.getPreviousOrLastCardId(savedCard.getId());
        LOGGER.info("previousCardId " + previousCardId);
        modelAndView.addObject("previousCardId", previousCardId);

        final String nextCardId = cardService.getNextOrFirstCardId(savedCard.getId());
        LOGGER.info("nextCardId " + nextCardId);
        modelAndView.addObject("nextCardId", nextCardId);

        modelAndView.setViewName("cardById");
        LOGGER.info("before show cardById.html");
        return modelAndView;
    }

    @GetMapping("/card/{id}/delete")
    public ModelAndView getCardDeleteForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("id: " + id);
        final Card card = cardService.getById(id);
        LOGGER.info("card getById: " + card);
        modelAndView.addObject("card", card);
        modelAndView.setViewName("cardDeleteById");
        LOGGER.info("before show cardDeleteById.html");
        return modelAndView;
    }

    @DeleteMapping("/card/{id}/delete")  // after delete card
    public ModelAndView deleteCard(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("id: " + id);

        final SetOfCards setOfCards = setOfCardsService.getById(cardService.getById(id).getSetOfCards().getId());
        LOGGER.info("setOfCards getById: " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        cardService.deleteById(id);
        LOGGER.info("card is deleted");

        List<Card> cards = cardService.getBySet(setOfCards);
        LOGGER.info("List<Card>: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");  // todo maybe go to editSet (because from here we delete card (and from show card))?
        LOGGER.info("before show setById.html");
        return modelAndView;
    }
}
