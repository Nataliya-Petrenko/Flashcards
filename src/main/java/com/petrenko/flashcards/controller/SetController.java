package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.CardIdQuestionDto;
import com.petrenko.flashcards.dto.SetEditDto;
import com.petrenko.flashcards.dto.SetFolderNameSetNameDescriptionDto;
import com.petrenko.flashcards.dto.SetViewByIdDto;
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
public class SetController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SetController.class);

    private final CardService cardService;
    private final SetOfCardsService setOfCardsService;
    private final FolderService folderService;

    @Autowired
    public SetController(final CardService cardService,
                         final SetOfCardsService setOfCardsService,
                         final FolderService folderService) {
        this.cardService = cardService;
        this.setOfCardsService = setOfCardsService;
        this.folderService = folderService;
    }

    @GetMapping("/set/create")
    public ModelAndView getCreateSetForm(ModelAndView modelAndView) {
        final SetFolderNameSetNameDescriptionDto setDto = new SetFolderNameSetNameDescriptionDto();
        modelAndView.addObject("setDto", setDto);
        modelAndView.setViewName("setCreate");
        return modelAndView;
    }

    @PostMapping("/set/create")
    public ModelAndView saveNewSet(@ModelAttribute SetFolderNameSetNameDescriptionDto setDto,
                                   BindingResult bindingResult,
                                   Principal principal,
                                   ModelAndView modelAndView) {
        LOGGER.trace("SetFolderNameSetNameDescriptionDto from form {}", setDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", setDto);
            modelAndView.addObject("setDto", setDto);
            modelAndView.setViewName("setCreate");
            return modelAndView;
        }
        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);
        final SetOfCards savedSetOfCards = setOfCardsService.saveSetFolderNameSetNameDescriptionDto(userId, setDto);

        modelAndView.setViewName("redirect:/set/" + savedSetOfCards.getId());
        return modelAndView;
    }

    @GetMapping("/set/create/{id}")  // with fill folder name by setId
    public ModelAndView getSetForm(@PathVariable("id") String id,
                                   ModelAndView modelAndView) {
        LOGGER.info("invoked with folder id {}", id);
        final SetFolderNameSetNameDescriptionDto setDto = new SetFolderNameSetNameDescriptionDto();
        final String folderName = folderService.getNameById(id);
        setDto.setFolderName(folderName);
        modelAndView.addObject("setDto", setDto);

        modelAndView.setViewName("setCreate");
        return modelAndView;
    }

    @GetMapping("/set/{id}")
    public ModelAndView getSetById(@PathVariable("id") String id,
                                   ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);

        final SetViewByIdDto setViewByIdDto = setOfCardsService.getSetViewByIdDto(id);
        modelAndView.addObject("setViewByIdDto", setViewByIdDto);

        final List<CardIdQuestionDto> cards = cardService.getBySetId(id);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");
        return modelAndView;
    }


    @GetMapping("/set/{id}/edit")
    public ModelAndView getSetEditForm(@PathVariable("id") String id,
                                       ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);
        final SetEditDto setEditDto = setOfCardsService.getSetEditDto(id);
        modelAndView.addObject("setEditDto", setEditDto);
        modelAndView.setViewName("setEdit");
        return modelAndView;
    }

    @PutMapping("/set/{id}/edit")
    public ModelAndView editSet(@ModelAttribute SetEditDto setEditDto,
                                Principal principal,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        LOGGER.info("setEditDto from form: {}", setEditDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", setEditDto);
            modelAndView.addObject("setEditDto", setEditDto);
            modelAndView.setViewName("setEdit");
            return modelAndView;
        }
        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);
        final SetOfCards savedSetOfCards = setOfCardsService.updateSetOfCardsBySetEditDto(userId, setEditDto);
        modelAndView.setViewName("redirect:/set/" + setEditDto.getId());
        return modelAndView;
    }

    @GetMapping("/set/{id}/delete")
    public ModelAndView getSetDeleteForm(@PathVariable("id") String id,
                                         ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);
        final SetEditDto setEditDto = setOfCardsService.getSetEditDto(id);
        modelAndView.addObject("setEditDto", setEditDto);

        final List<CardIdQuestionDto> cards = cardService.getBySetId(id);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setDeleteById");
        return modelAndView;
    }

    @DeleteMapping("/set/{id}/delete")
    public ModelAndView deleteSet(@PathVariable("id") String id,
                                  ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);
        setOfCardsService.deleteAllById(id);
        modelAndView.setViewName("redirect:/folder");
        return modelAndView;
    }

}
