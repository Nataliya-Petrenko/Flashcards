package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.KeyWord;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;
    private final KeyWordService keyWordService;

    @Autowired
    public CardController(final CardService cardService, final KeyWordService keyWordService) {
        this.cardService = cardService;
        this.keyWordService = keyWordService;
    }

    @GetMapping
    public ModelAndView getTest(ModelAndView modelAndView) {
        Card card = new Card();
        card.setQuestion("Question ".repeat(5));
        card.setShortAnswer("ShortAnswer ".repeat(10));
        card.setLongAnswer("LongAnswer ".repeat(50));
        List<KeyWord> keyWords = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            KeyWord keyWord = new KeyWord("key word " + i);
            keyWords.add(keyWord);
            keyWordService.save(keyWord);
        }
        card.setKeyWords(keyWords);
        cardService.save(card);
        modelAndView.addObject("card", card);
        System.out.println("Card: " + card);
        modelAndView.setViewName("cardView");
        return modelAndView;
    }

//    @GetMapping("/{id}")
//    public ModelAndView getCard(@PathVariable String id, ModelAndView modelAndView) {
//        if (id != null && !id.isBlank()) {
//            final Card card = cardService.getById(id);
//            modelAndView.addObject("card", card);
//        }
//        System.out.printf("Get for card with id = %s/n", id);
//        modelAndView.setViewName("cardView");
//        return modelAndView;
//    }

    @PostMapping("/create")
    public ModelAndView saveNewCard(@ModelAttribute @Valid Card card, BindingResult bindingResult,
                                    ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("card", card);
            modelAndView.setViewName("cardFormView");
            return modelAndView;
        }
        cardService.save(card);
        System.out.println(card);
        modelAndView.setViewName("cardView");
        return modelAndView;
    }

    @PutMapping("/edit")  // todo & ("/{id}") ?
    public ModelAndView updateCard(@ModelAttribute @Valid Card card, BindingResult bindingResult,
                                   ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("card", card);
            modelAndView.setViewName("cardFormView");
            return modelAndView;
        }
        cardService.save(card);  // todo check correct work for update
        System.out.println(card);
        modelAndView.setViewName("cardView");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable String id) {
        if (id != null) {
            cardService.deleteById(id);
        }

        // go to set
    }


}
