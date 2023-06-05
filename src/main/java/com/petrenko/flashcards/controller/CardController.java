package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping()
public class CardController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CardController.class);
    private final CardService cardService;

    @Autowired
    public CardController(final CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/")
    public ModelAndView getAllCards(ModelAndView modelAndView) {
        final List<CardIdQuestionDto> cards = cardService.getAll();
        modelAndView.addObject("cards", cards);
        modelAndView.setViewName("search");
        return modelAndView;
    }

    @GetMapping("/pagination") // todo depending from page in site and cards in page
    public ModelAndView getAllCardsPage(ModelAndView modelAndView) {
//        int currentSize = size.orElse(5);
        int currentSize = 5;
//        int currentPage = page.orElse(1);
        int currentPage = 1;
        Pageable pageable = PageRequest.of(currentPage - 1, currentSize, Sort.by("question").descending().and(Sort.by("id")));
        final Page<CardIdQuestionDto> cardsPage = cardService.getAllPage(pageable);

        modelAndView.addObject("cards", cardsPage.getContent());
        modelAndView.setViewName("search");
        return modelAndView;
    }

    @PutMapping("/")
    public ModelAndView searchUser(@RequestParam("search") String search,
                                   ModelAndView modelAndView) {
        final List<CardIdQuestionDto> cards = cardService.getBySearch(search);
        modelAndView.addObject("cards", cards);
        modelAndView.setViewName("search");
        return modelAndView;
    }

    @GetMapping("/card/create")
    public ModelAndView getCreateCardForm(ModelAndView modelAndView) {
        CardCreatingDto cardCreatingDto = new CardCreatingDto();
        modelAndView.addObject("card", cardCreatingDto);
        modelAndView.setViewName("cardCreate");
        return modelAndView;
    }

    @GetMapping("/card/create/{id}")  // with fill set name and folder name
    public ModelAndView getCreateCardFormWithSet(@PathVariable("id") String id,
                                                 ModelAndView modelAndView) {
        LOGGER.trace("folder id from link: {}", id);
        final CardCreatingDto cardCreatingDto = cardService.getCardCreatingDtoBySetId(id);
        modelAndView.addObject("card", cardCreatingDto);
        modelAndView.setViewName("cardCreate");
        return modelAndView;
    }

    @PostMapping("/card/create")
    public ModelAndView saveNewCard(@ModelAttribute CardCreatingDto cardCreatingDto,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView,
                                    Principal principal) {
        LOGGER.trace("cardCreatingDto from form {}", cardCreatingDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", cardCreatingDto);
            modelAndView.addObject("card", cardCreatingDto);
            modelAndView.setViewName("cardCreate");
            return modelAndView;
        }
        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);
        final Card savedCard = cardService.saveCardCreatingDtoToCard(userId, cardCreatingDto);
        modelAndView.setViewName("redirect:/card/" + savedCard.getId());
        return modelAndView;
    }

    @GetMapping("/card/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id,
                                    ModelAndView modelAndView) {
        LOGGER.trace("card id from link: {}", id);
        final CardByIdDto cardByIdDto = cardService.getCardByIdDto(id);
        modelAndView.addObject("card", cardByIdDto);
        modelAndView.setViewName("cardById");
        return modelAndView;
    }

    @GetMapping("/card/{id}/learning")
    public ModelAndView getCardByIdLearning(@PathVariable("id") String id,
                                            ModelAndView modelAndView) {
        LOGGER.trace("card id from link: {}", id);
        final CardByIdDto cardByIdDto = cardService.getCardByIdDto(id);
        modelAndView.addObject("card", cardByIdDto);
        modelAndView.setViewName("cardLearning");
        return modelAndView;
    }

    @GetMapping("/card/{id}/edit")
    public ModelAndView getCardEditForm(@PathVariable("id") String id,
                                        ModelAndView modelAndView) {
        LOGGER.trace("card id from link: {}", id);
        final CardEditDto cardEditDto = cardService.getCardEditDto(id);
        modelAndView.addObject("cardEditDto", cardEditDto);
        modelAndView.setViewName("cardEdit");
        return modelAndView;
    }

    @PutMapping("/card/{id}/edit")
    public ModelAndView editCard(@ModelAttribute CardEditDto cardEditDto,
                                 Principal principal,
                                 BindingResult bindingResult,
                                 ModelAndView modelAndView) {
        LOGGER.trace("cardEditDto from form: {}", cardEditDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", cardEditDto);
            modelAndView.addObject("cardEditDto", cardEditDto);
            modelAndView.setViewName("cardEdit");
            return modelAndView;
        }

        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);
        final Card savedCard = cardService.updateCardByCardEditDto(userId, cardEditDto);
        modelAndView.setViewName("redirect:/card/" + savedCard.getId());
        return modelAndView;
    }

    @GetMapping("/card/{id}/delete")
    public ModelAndView getCardDeleteForm(@PathVariable("id") String id,
                                          ModelAndView modelAndView) {
        LOGGER.trace("card id from link: {}", id);
        final CardEditDto cardEditDto = cardService.getCardEditDto(id);
        modelAndView.addObject("cardEditDto", cardEditDto);
        modelAndView.setViewName("cardDeleteById");
        return modelAndView;
    }

    @DeleteMapping("/card/{id}/delete")
    public ModelAndView deleteCard(@PathVariable("id") String id,
                                   ModelAndView modelAndView) {
        LOGGER.trace("card id from link: {}", id);
        cardService.deleteById(id);
        modelAndView.setViewName("redirect:/folder");
        return modelAndView;
    }
}
