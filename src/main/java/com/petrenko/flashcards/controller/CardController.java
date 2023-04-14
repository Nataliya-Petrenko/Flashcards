package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.SetOfCardsService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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

    @GetMapping("/card/create")
    public ModelAndView getCreateCardForm(ModelAndView modelAndView) {
        LOGGER.info("invoked");

        CardCreatingDto cardCreatingDto = new CardCreatingDto();
        LOGGER.info("new cardCreatingDto() {}", cardCreatingDto);
        modelAndView.addObject("card", cardCreatingDto);

        modelAndView.setViewName("cardCreate");
        LOGGER.info("before show cardCreate");
        return modelAndView;
    }

    @PostMapping("/card/create")
    public ModelAndView saveNewCard(@ModelAttribute CardCreatingDto cardCreatingDto,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView,
                                   Principal principal) {
        LOGGER.info("cardCreatingDto from form {}", cardCreatingDto);
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", cardCreatingDto);
            modelAndView.addObject("card", cardCreatingDto);
            modelAndView.setViewName("cardCreate");
            return modelAndView;
        }

        String userId = principal.getName(); // userName = id
        LOGGER.info("userId {}", userId);

        Card savedCard = cardService.saveCardCreatingDtoToCard(userId, cardCreatingDto); // todo delete get savedCard after checking work
        LOGGER.info("card saved {}", savedCard);

        String red = "redirect:/card/" + savedCard.getId();
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);

        return modelAndView;
    }

    @GetMapping("/card/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id, ModelAndView modelAndView, Principal principal) {
        String userId = principal.getName();
        final String previousCardId = cardService.getPreviousOrLastCardId(userId, id);
        LOGGER.info("previousCardId " + previousCardId);
        modelAndView.addObject("previousCardId", previousCardId);

        final String nextCardId = cardService.getNextOrFirstCardId(userId, id);
        LOGGER.info("nextCardId " + nextCardId);
        modelAndView.addObject("nextCardId", nextCardId);

        final Card card = cardService.getById(id);
        modelAndView.addObject("card", card);  // todo show text from DB with formatting
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

//    @GetMapping("/card/create")
//    public ModelAndView getCreateCardForm(ModelAndView modelAndView) {
//        LOGGER.info("invoked");
//
//        Card card = new Card();
//        LOGGER.info("new Card() " + card);
//        modelAndView.addObject("card", card);
//
//        modelAndView.setViewName("cardCreate");
//        LOGGER.info("before show cardCreate");
//        return modelAndView;
//    }
//
//    @PostMapping("/card/create")  // after created card
//    public ModelAndView createCard(@ModelAttribute Card card,
//                                   BindingResult bindingResult,
//                                   ModelAndView modelAndView,
//                                   Principal principal) {
//        LOGGER.info("card from form " + card);
//        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("card", card);
//            modelAndView.setViewName("cardCreate");
//            return modelAndView;
//        }
//
//        String userId = principal.getName();
//        Card savedCard = cardService.save(userId, card);  // userName = id
//        LOGGER.info("card saved " + savedCard);
//
//        final SetOfCards setOfCards = card.getSetOfCards();
//        LOGGER.info("setOfCards: " + setOfCards);
//        modelAndView.addObject("setOfCards", setOfCards);
//
//        List<Card> cards = cardService.getBySet(card.getSetOfCards());
//        LOGGER.info("List<Card>: " + cards);
//        modelAndView.addObject("cards", cards);
//
//        modelAndView.setViewName("setById"); // todo redirect
//        LOGGER.info("before show setById.html");
//
////        modelAndView.setViewName("redirect:/profile");
////        LOGGER.info("before redirect:/profile");
//        return modelAndView;
//    }

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
                                 ModelAndView modelAndView,
                                 Principal principal) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("card", card);
            modelAndView.setViewName("cardEdit");
            return modelAndView;
        }
        LOGGER.info("card from form " + card);

        String userId = principal.getName();

        Card savedCard = cardService.save(userId, card);
        LOGGER.info("card saved " + savedCard);
        modelAndView.addObject("card", savedCard);

        final String previousCardId = cardService.getPreviousOrLastCardId(userId, savedCard.getId());
        LOGGER.info("previousCardId " + previousCardId);
        modelAndView.addObject("previousCardId", previousCardId);

        final String nextCardId = cardService.getNextOrFirstCardId(userId, savedCard.getId());
        LOGGER.info("nextCardId " + nextCardId);
        modelAndView.addObject("nextCardId", nextCardId);

        modelAndView.setViewName("cardById");
        LOGGER.info("before show cardById.html");
//        modelAndView.setViewName("redirect:/getCardById");
//        LOGGER.info("before redirect:/getCardById");
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
