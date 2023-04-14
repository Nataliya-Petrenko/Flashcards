package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.*;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.FolderService;
import com.petrenko.flashcards.service.SetOfCardsService;
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

    @Autowired
    public CardController(final CardService cardService) {
        this.cardService = cardService;
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

    @GetMapping("/card/create/{id}")  // with fill set name and folder name
    public ModelAndView getCreateCardFormWithSet(@PathVariable("id") String id,
                                                 ModelAndView modelAndView) {
        LOGGER.info("set id {}", id);

        CardCreatingDto cardCreatingDto = cardService.getCardCreatingDtoBySetId(id);
        LOGGER.info("getCardCreatingDtoBySetId() {}", cardCreatingDto);
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

        String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        Card savedCard = cardService.saveCardCreatingDtoToCard(userId, cardCreatingDto); // todo delete get savedCard after checking work
        LOGGER.info("card saved {}", savedCard);

        String red = "redirect:/card/" + savedCard.getId();
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);

        return modelAndView;
    }

    @GetMapping("/card/{id}")
    public ModelAndView getCardById(@PathVariable("id") String id,
                                    ModelAndView modelAndView) {
        LOGGER.info("card id from link: {}", id);

        CardByIdDto cardByIdDto = cardService.getCardByIdDto(id);
        modelAndView.addObject("card", cardByIdDto);
        LOGGER.info("cardByIdDto: {}", cardByIdDto);

        modelAndView.setViewName("cardById");
        return modelAndView;
    }

    @GetMapping("/card/{id}/edit")
    public ModelAndView getCardEditForm(@PathVariable("id") String id,
                                        ModelAndView modelAndView) {
        LOGGER.info("card id from link: {}", id);

        final CardEditDto cardEditDto = cardService.getCardEditDto(id);
        LOGGER.info("cardEditDto: {}", cardEditDto);
        modelAndView.addObject("cardEditDto", cardEditDto);

        modelAndView.setViewName("cardEdit");
        LOGGER.info("before show cardEdit.html");
        return modelAndView;
    }

    @PutMapping("/card/{id}/edit")
    public ModelAndView editCard(@ModelAttribute CardEditDto cardEditDto,
                                 Principal principal,
                                 BindingResult bindingResult,
                                 ModelAndView modelAndView) {
        LOGGER.info("cardEditDto from form: {}", cardEditDto);
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", cardEditDto);
            modelAndView.addObject("cardEditDto", cardEditDto);
            modelAndView.setViewName("cardEdit");
            return modelAndView;
        }

        final String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        Card savedCard = cardService.updateCardByCardEditDto(userId, cardEditDto);
        LOGGER.info("savedCard: {}", savedCard);

        String red = "redirect:/card/" + savedCard.getId();
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);

        return modelAndView;
    }

    @GetMapping("/card/{id}/delete")
    public ModelAndView getCardDeleteForm(@PathVariable("id") String id,
                                          ModelAndView modelAndView) {
        LOGGER.info("card id from link: {}", id);

        final CardEditDto cardEditDto = cardService.getCardEditDto(id);
        LOGGER.info("cardEditDto: {}", cardEditDto);
        modelAndView.addObject("cardEditDto", cardEditDto);

        modelAndView.setViewName("cardDeleteById");
        LOGGER.info("before show cardDeleteById.html");
        return modelAndView;
    }

    @DeleteMapping("/card/{id}/delete")
    public ModelAndView deleteCard(@PathVariable("id") String id,
                                   ModelAndView modelAndView) {
        LOGGER.info("card id from link: {}", id);

        cardService.deleteById(id);
        LOGGER.info("card is deleted");

        modelAndView.setViewName("redirect:/folder");
        LOGGER.info("before redirect:/folder");

        return modelAndView;
    }
}
