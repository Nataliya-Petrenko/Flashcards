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

import java.util.List;

@Controller
@RequestMapping()
public class SetController {

    private final CardService cardService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public SetController(final CardService cardService,
                         final SetOfCardsService setOfCardsService) {
        this.cardService = cardService;
        this.setOfCardsService = setOfCardsService;
    }

//    @GetMapping("/set")
//    public ModelAndView getSet(ModelAndView modelAndView) {
//        System.out.println("Get set");
//        SetOfCards setOfCards = setOfCardsService.newSet();
//        List<Card> cards = cardService.getBySet(setOfCards);
////        modelAndView.addObject("set", setOfCards);
//        modelAndView.addObject("cards", cards);
//        modelAndView.setViewName("setView");
//        return modelAndView;
//    }

    @GetMapping("/set/{id}")
    public ModelAndView getSetById(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/set/{id}) id: " + id);
        final SetOfCards setOfCards = setOfCardsService.getById(id);
        System.out.println("@GetMapping(/set/{id}) setOfCardsService.getById(id): " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@GetMapping(/set/{id})  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setViewById");
        System.out.println("@GetMapping(/set/{id}) before show setViewById.html");
        return modelAndView;
    }

    @GetMapping("/set/create")
    public ModelAndView getSetForm(ModelAndView modelAndView) {
        System.out.println("@GetMapping(/set/create)");
        SetOfCards setOfCards = new SetOfCards();
        System.out.println("@GetMapping(/set/create) new SetOfCards " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        modelAndView.setViewName("createSetView");
        System.out.println("@GetMapping(/set/create) before show createSetView.html");
        return modelAndView;
    }

    @PostMapping("/set/create")  // after created set
    public ModelAndView saveNewSet(@ModelAttribute SetOfCards setOfCards,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView) {
        System.out.println("@PostMapping(/set) " + setOfCards);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("setOfCards", setOfCards);
            modelAndView.setViewName("createSetView");
            return modelAndView;
        }

        SetOfCards savedSetOfCards = setOfCardsService.save(setOfCards);
        System.out.println("@PostMapping(/set) savedSetOfCards " + savedSetOfCards);
        modelAndView.addObject("setOfCards", savedSetOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@PostMapping(/set)  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setViewById");
        System.out.println("@PostMapping(/set) before show setViewById.html");
        return modelAndView;
    }


    @GetMapping("/set/{id}/edit")
    public ModelAndView getSetEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/set/{id}/edit) id: " + id);
        final SetOfCards setOfCards = setOfCardsService.getById(id);
        System.out.println("@GetMapping(/set/{id}/edit) setOfCardsService.getById(id): " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@GetMapping(/set/{id}/edit)  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);
        modelAndView.setViewName("editSetView");
        System.out.println("@GetMapping(/set/{id}/edit) before show editSetView.html");
        return modelAndView;
    }

    @PutMapping("/set/{id}/edit")  // after edited set
    public ModelAndView editSet(@PathVariable("id") String id,   // todo save id into Dto in view
                                @ModelAttribute SetOfCards setOfCards,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        System.out.println("@PutMapping(/set/{id}/edit) id: " + id);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("setOfCards", setOfCards);
            modelAndView.setViewName("editSetView");
            return modelAndView;
        }
        SetOfCards savedSetOfCards = setOfCardsService.save(setOfCards);
        System.out.println("@PutMapping(/set/{id}/edit) setOfCards saved " + savedSetOfCards);
        modelAndView.addObject("setOfCards", savedSetOfCards);

        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@PutMapping(/set/{id}/edit)  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setViewById");
        System.out.println("@PutMapping(/set/{id}/edit) before show setViewById.html");
        return modelAndView;
    }

    @GetMapping("/set/{id}/delete")
    public ModelAndView getSetDeleteForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/set/{id}/delete) id: " + id);
        final SetOfCards setOfCards = setOfCardsService.getById(id);
        System.out.println("@GetMapping(/set/{id}/delete) setOfCards getById: " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        modelAndView.setViewName("deleteSetViewById");

        List<Card> cards = cardService.getBySet(setOfCards);
        System.out.println("@GetMapping(/set/{id})  List<Card> getBySet: " + cards);
        modelAndView.addObject("cards", cards);

        System.out.println("@GetMapping(/set/{id}/delete) before show deleteSetViewById.html");
        return modelAndView;
    }

    @DeleteMapping("/set/{id}/delete")  // after delete card
    public ModelAndView deleteSet(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@DeleteMapping(/delete/{id}) id: " + id);

        // get folder and go to folder view
//        final SetOfCards setOfCards = setOfCardsService.getById(cardService.getById(id).getSetOfCards().getId());
//        System.out.println("@DeleteMapping(/delete/{id}) setOfCards getById: " + setOfCards);
//        modelAndView.addObject("setOfCards", setOfCards);

        setOfCardsService.deleteById(id);
        System.out.println("@DeleteMapping(/delete/{id}) setOfCards is deleted");

//        List<Card> cards = cardService.getBySet(setOfCards);
//        System.out.println("@DeleteMapping(/delete/{id}) List<Card>: " + cards);
//        modelAndView.addObject("cards", cards);

//        modelAndView.setViewName("setViewById");

// todo go to folder or profile
        SetOfCards setOfCards = new SetOfCards();
        System.out.println("@DeleteMapping(/delete/{id}) new SetOfCards " + setOfCards);
        modelAndView.addObject("setOfCards", setOfCards);
        modelAndView.setViewName("createSetView");

        System.out.println("@DeleteMapping(/delete/{id}) before show createSetView.html");
        return modelAndView;
    }


}
