package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ModelAndView getCard(ModelAndView modelAndView) {
        final Card card = cardService.getById("12345");
        modelAndView.addObject("card", card);
        modelAndView.setViewName("cardView");
        return modelAndView;
    }
}
