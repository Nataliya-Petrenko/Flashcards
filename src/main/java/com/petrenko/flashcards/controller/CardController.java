package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.SetOfCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping()
public class CardController {

    private final CardService cardService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public CardController(final CardService cardService,
                          final SetOfCardsService setOfCardsService) {
        this.cardService = cardService;
        this.setOfCardsService = setOfCardsService;
    }

    @GetMapping("/card/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id, ModelAndView modelAndView) {
            final String previousCardId = cardService.getPreviousOrLastCardId(id);
            System.out.println("previousCardId " + previousCardId);
            modelAndView.addObject("previousCardId", previousCardId);

            final String nextCardId = cardService.getNextOrFirstCardId(id);
            System.out.println("nextCardId " + nextCardId);
            modelAndView.addObject("nextCardId", nextCardId);

            final Card card = cardService.getById(id);
            modelAndView.addObject("card", card);
            System.out.println("Card by id: " + card);

            modelAndView.setViewName("cardViewById");
            return modelAndView;
    }

    @GetMapping("/card/create")
    public ModelAndView getCardForm(ModelAndView modelAndView) {
        CardCreatingDto cardCreatingDto = new CardCreatingDto();
        modelAndView.addObject("cardCreatingDto", cardCreatingDto);

        modelAndView.setViewName("createCardView");
        return modelAndView;
    }

    @PostMapping("/card")  // after created card
    public ModelAndView saveNewCard(@ModelAttribute CardCreatingDto cardCreatingDto,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView) {
        System.out.println("GetPost Creating a cardCreatingDto " + cardCreatingDto);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("cardCreatingDto", cardCreatingDto);
            modelAndView.setViewName("createCardView");
            return modelAndView;
        }
        Card card = cardService.saveToCard(cardCreatingDto);

        modelAndView.addObject("card", card);
        modelAndView.setViewName("cardView");
        return modelAndView;
    }

//    @PostMapping("/create")
//    public ModelAndView saveNewCard(@ModelAttribute @Valid Card card, BindingResult bindingResult,
//                                    ModelAndView modelAndView) {
//        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("card", card);
//            modelAndView.setViewName("editCardView.html");
//            return modelAndView;
//        }
//        cardService.save(card);
//        System.out.println(card);
//        modelAndView.setViewName("cardView");
//        return modelAndView;
//    }

    @GetMapping("/card/{id}/edit")
    public ModelAndView getCardEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        final Card card = cardService.getById(id);
        modelAndView.addObject("card", card);
        modelAndView.setViewName("editCardView");
        System.out.println("getCardEditForm " + card);
        return modelAndView;
    }

    @PutMapping("/card/edit")
    public ModelAndView editCard(@ModelAttribute Card card, BindingResult bindingResult,
                                   ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("card", card);
            modelAndView.setViewName("editCardView");
            return modelAndView;
        }
        cardService.editCard(card);
        modelAndView.addObject("card", card);
        modelAndView.setViewName("cardView");
        return modelAndView;
    }

//
//    @DeleteMapping("/{id}")
//    public void deleteCard(@PathVariable String id) {
//        if (id != null) {
//            cardService.deleteById(id);
//        }
//
//        // go to set
//    }


}
