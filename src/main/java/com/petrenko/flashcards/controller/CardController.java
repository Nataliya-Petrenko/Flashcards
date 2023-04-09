package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.CardCreatingDto;
import com.petrenko.flashcards.dto.CardEditingDto;
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

    @GetMapping("/card/create/{id}")  // with fill set name
    public ModelAndView getCardForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/card/create/{id}) with fill set name");

        CardCreatingDto cardCreatingDto = new CardCreatingDto();
        System.out.println("@GetMapping(/card/create/{id}) new CardCreatingDto() " + cardCreatingDto);

        SetOfCards setOfCards = setOfCardsService.getById(id);
        System.out.println("@GetMapping(/card/create/{id}) setOfCards getById" + setOfCards);
        cardCreatingDto.setSetOfCardsName(setOfCards.getName());
        System.out.println("@GetMapping(/card/create/{id}) CardCreatingDto with name of set " + cardCreatingDto);
        modelAndView.addObject("cardCreatingDto", cardCreatingDto);
//        modelAndView.addObject("setOfCardsId", id);

        modelAndView.setViewName("createCardView");
        System.out.println("@GetMapping(/card/create/{id}) before show createCardView");
        return modelAndView;
    }

    @GetMapping("/card/create")
    public ModelAndView getCardFormWithSet(ModelAndView modelAndView) {
        System.out.println("@GetMapping(/card/create)");

        CardCreatingDto cardCreatingDto = new CardCreatingDto();
        System.out.println("@GetMapping(/card/create) new CardCreatingDto() " + cardCreatingDto);
        modelAndView.addObject("cardCreatingDto", cardCreatingDto);

        modelAndView.setViewName("createCardView");
        System.out.println("@GetMapping(/card/create) before show createCardView");
        return modelAndView;
    }

    @PostMapping("/card")  // after created card
    public ModelAndView saveNewCard(@ModelAttribute CardCreatingDto cardCreatingDto,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView) {
        System.out.println("@PostMapping(/card) after created card " + cardCreatingDto);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("cardCreatingDto", cardCreatingDto);
            modelAndView.setViewName("createCardView");
            return modelAndView;
        }
        Card card = cardService.saveToCard(cardCreatingDto);
        System.out.println("@PostMapping(/card) card saved " + card);

//        modelAndView.addObject("card", card);
//
//        final String previousCardId = cardService.getPreviousOrLastCardId(card.getId());
//        System.out.println("previousCardId " + previousCardId);
//        modelAndView.addObject("previousCardId", previousCardId);
//
//        final String nextCardId = cardService.getNextOrFirstCardId(card.getId());
//        System.out.println("nextCardId " + nextCardId);
//        modelAndView.addObject("nextCardId", nextCardId);
//
//        modelAndView.setViewName("cardViewById");
//        return modelAndView; // todo go to set by id


        final SetOfCards setOfCards = setOfCardsService.getByName(cardCreatingDto.getSetOfCardsName())
                .orElseThrow(IllegalArgumentException::new);
        System.out.println("@GetMapping(/card/create) setOfCards getByName: " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@GetMapping(/card/create) List<Card>: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setViewById");
        System.out.println("@GetMapping(/card/create) before show setViewById.html");
        return modelAndView;
    }

    @GetMapping("/card/{id}/edit")
    public ModelAndView getCardEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        final CardEditingDto cardEditingDto = cardService.getCardEditingDto(id);
        System.out.println("getCardEditingDto: " + cardEditingDto);
        modelAndView.addObject("cardEditingDto", cardEditingDto);
        modelAndView.setViewName("editCardView");
        return modelAndView;
    }

    @PutMapping("/edit/{id}")  // after edited card
    public ModelAndView editCard(@PathVariable("id") String id,   // todo save id into cardEditingDto in view
                                 @ModelAttribute CardEditingDto cardEditingDto,
                                 BindingResult bindingResult,
                                 ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("cardEditingDto", cardEditingDto);
            modelAndView.setViewName("editCardView");
            return modelAndView;
        }
        System.out.println("editCard after edited card");
        cardEditingDto.setId(id);
        Card card = cardService.editCardByCardEditingDto(cardEditingDto);
        System.out.println("card by editCardByCardEditingDto" + card);
        modelAndView.addObject("card", card);

        final String previousCardId = cardService.getPreviousOrLastCardId(card.getId());
        System.out.println("previousCardId " + previousCardId);
        modelAndView.addObject("previousCardId", previousCardId);

        final String nextCardId = cardService.getNextOrFirstCardId(card.getId());
        System.out.println("nextCardId " + nextCardId);
        modelAndView.addObject("nextCardId", nextCardId);

        modelAndView.setViewName("cardViewById");
        return modelAndView;
    }

    @GetMapping("/card/{id}/delete")
    public ModelAndView getCardDeleteForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/card/{id}/delete) id: " + id);
        final Card card = cardService.getById(id);
        System.out.println("@GetMapping(/card/{id}/delete) card getById: " + card);
        modelAndView.addObject("card", card);
        modelAndView.setViewName("deleteCardViewById");
        System.out.println("@GetMapping(/card/{id}/delete) before show deleteCardViewById.html");
        return modelAndView;
    }

    @DeleteMapping("/delete/{id}")  // after delete card
    public ModelAndView deleteCard(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@DeleteMapping(/delete/{id}) id: " + id);

        final SetOfCards setOfCards = setOfCardsService.getById(cardService.getById(id).getSetOfCards().getId());
        System.out.println("@DeleteMapping(/delete/{id}) setOfCards getById: " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        cardService.deleteById(id);
        System.out.println("@DeleteMapping(/delete/{id}) card is deleted");

        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@DeleteMapping(/delete/{id}) List<Card>: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setViewById");
        System.out.println("@DeleteMapping(/delete/{id}) before show setViewById.html");
        return modelAndView;
    }
}
