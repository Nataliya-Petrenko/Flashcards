package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.KeyWordService;
import com.petrenko.flashcards.service.SetOfCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping()
public class SetController {

    private final CardService cardService;
    private final KeyWordService keyWordService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public SetController(final CardService cardService,
                         final KeyWordService keyWordService,
                         final SetOfCardsService setOfCardsService) {
        this.cardService = cardService;
        this.keyWordService = keyWordService;
        this.setOfCardsService = setOfCardsService;
    }

    @GetMapping("/set")
    public ModelAndView getSet(ModelAndView modelAndView) {
        System.out.println("Get set");
        SetOfCards setOfCards = setOfCardsService.newSet();
        List<Card> cards = cardService.getBySet(setOfCards);
//        modelAndView.addObject("set", setOfCards);
        modelAndView.addObject("cards", cards);
        modelAndView.setViewName("setView");
        return modelAndView;
    }

    @GetMapping("/set/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id, ModelAndView modelAndView) {
        if (id != null && !id.isBlank()) {
            final SetOfCards setOfCards = setOfCardsService.getById(id);
//            modelAndView.addObject("set", setOfCards);
//            System.out.println("setOfCards by id: " + setOfCards);
            List<Card> cards = cardService.getBySet(setOfCards);
            modelAndView.addObject("cards", cards);
            modelAndView.setViewName("setViewById");
            return modelAndView;
        }
        modelAndView.setViewName("setViewById");
        return modelAndView;
    }
//
//    @GetMapping("/card/create")
//    public ModelAndView getArticleForm(ModelAndView modelAndView) {
//        Card card = new Card();
//        modelAndView.addObject("card", card);
//
//        List<String> studyPriorities = Arrays.stream(StudyPriority.values())
//                .map(Enum::toString)
//                .toList();
//        modelAndView.addObject("priorities", studyPriorities);
//
//        List<String> knowledgeLevels = Arrays.stream(KnowledgeLevel.values())
//                .map(Enum::toString)
//                .toList();
//        modelAndView.addObject("knowledgeLevels", knowledgeLevels);
//
//        modelAndView.setViewName("editCardView");
//        return modelAndView;
//    }
//
//    @PostMapping("/card")
//    public ModelAndView saveNewCard(@ModelAttribute Card card,
//                                    @RequestParam("inputWords") String inputWords,
//                                    @RequestParam("set") String setName,
//                                    BindingResult bindingResult,
//                                    ModelAndView modelAndView) {
//        System.out.println("GetPost Creating a card " + card);
//        if (bindingResult.hasErrors()) {
//            System.out.println("Creating card has error");
//            modelAndView.addObject("card", card);
//            modelAndView.setViewName("editCardView.html");
//            return modelAndView;
//        }
//        System.out.println("After if");
//
//        List<String> wordsList = Arrays.asList(inputWords.split(","));
//        List<KeyWord> keyWords = new LinkedList<>();
//        wordsList.forEach(w -> {
//            KeyWord keyWord = new KeyWord();
//            keyWord.setName(w);
//            keyWords.add(keyWord);
//            keyWordService.save(keyWord);
//        });
//        card.setKeyWords(keyWords);
//
//        SetOfCards setOfCards = new SetOfCards();
//        setOfCards.setName(setName);
//        setOfCardsService.save(setOfCards);
//        card.setSetOfCards(setOfCards);
//
//        cardService.save(card);
//        System.out.println(card);
//        modelAndView.addObject("card", card);
//        modelAndView.setViewName("cardView");
//        return modelAndView;
//    }

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

//    @PutMapping("/edit")  // todo & ("/{id}") ?
//    public ModelAndView updateCard(@ModelAttribute @Valid Card card, BindingResult bindingResult,
//                                   ModelAndView modelAndView) {
//        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("card", card);
//            modelAndView.setViewName("cardFormView");
//            return modelAndView;
//        }
//        cardService.save(card);  // todo check correct work for update
//        System.out.println(card);
//        modelAndView.setViewName("cardView");
//        return modelAndView;
//    }
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
