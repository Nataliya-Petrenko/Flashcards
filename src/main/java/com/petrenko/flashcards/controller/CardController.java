package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.KeyWord;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.KeyWordService;
import com.petrenko.flashcards.service.SetOfCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public CardController(final CardService cardService,
                          final KeyWordService keyWordService,
                          final SetOfCardsService setOfCardsService) {
        this.cardService = cardService;
        this.keyWordService = keyWordService;
        this.setOfCardsService = setOfCardsService;
    }

//    @Transactional // todo trans for service!
    @GetMapping
    public ModelAndView getTest(ModelAndView modelAndView) {
        Card card = new Card();
        System.out.println("Created new card");
        card.setQuestion("Question ".repeat(5));
        card.setShortAnswer("ShortAnswer ".repeat(10));
        card.setLongAnswer("LongAnswer ".repeat(50));

        SetOfCards setOfCards = new SetOfCards();
        System.out.println("Created new setOfCards");
        setOfCards.setName("NewSet");
//        setOfCards.getCards().add(card);
        card.setSetOfCards(setOfCards);
        setOfCardsService.save(setOfCards);
        System.out.println("Saved setOfCards");

        List<KeyWord> keyWords = new LinkedList<>();
        System.out.println("Created new List<KeyWord>");
        for (int i = 0; i < 5; i++) {
            KeyWord keyWord = new KeyWord();
            keyWord.setName("key word " + i);
            keyWords.add(keyWord);
            keyWordService.save(keyWord);
        }
        card.setKeyWords(keyWords);

        cardService.save(card);
        System.out.println("Saved card: " + card);
        modelAndView.addObject("card", card);
        System.out.println("addObject card");
        modelAndView.setViewName("cardView");
        System.out.println("modelAndView.setViewName");
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
