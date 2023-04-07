package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.dto.CardViewByIdDto;
import com.petrenko.flashcards.model.*;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping()
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

    @GetMapping("/card/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id, ModelAndView modelAndView) {
        if (id != null && !id.isBlank()) {
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
        modelAndView.setViewName("cardViewById");
        return modelAndView;
    }

    @GetMapping("/card/create")
    public ModelAndView getCardForm(ModelAndView modelAndView) {
        CardCreatingDto cardCreatingDto = new CardCreatingDto();
        modelAndView.addObject("cardCreatingDto", cardCreatingDto);

        List<String> studyPriorities = Arrays.stream(StudyPriority.values())
                .map(Enum::toString)
                .toList();
        modelAndView.addObject("priorities", studyPriorities);

        List<String> knowledgeLevels = Arrays.stream(KnowledgeLevel.values())
                .map(Enum::toString)
                .toList();
        modelAndView.addObject("knowledgeLevels", knowledgeLevels);

        modelAndView.setViewName("editCardView");
        return modelAndView;
    }

    @PostMapping("/card")  // after created card
    public ModelAndView saveNewCard(@ModelAttribute CardCreatingDto cardCreatingDto,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView) {
        System.out.println("GetPost Creating a cardCreatingDto " + cardCreatingDto);
        if (bindingResult.hasErrors()) {
            System.out.println("Creating card has error");
            modelAndView.addObject("cardCreatingDto", cardCreatingDto);
            modelAndView.setViewName("editCardView.html");
            return modelAndView;
        }
        System.out.println("After if");
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

//    @PutMapping("/card/{id}") // todo & ("/{id}") ?
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
