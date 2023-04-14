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
        LOGGER.info("invoked");

        SetFolderNameSetNameDescriptionDto setDto = new SetFolderNameSetNameDescriptionDto();
        LOGGER.info("new setFolderNameSetNameDescriptionDto {}", setDto);
        modelAndView.addObject("setDto", setDto);

        modelAndView.setViewName("setCreate");
        LOGGER.info("before show setCreate.html");
        return modelAndView;
    }

    @PostMapping("/set/create")
    public ModelAndView saveNewSet(@ModelAttribute SetFolderNameSetNameDescriptionDto setDto,
                                   BindingResult bindingResult,
                                   Principal principal,
                                   ModelAndView modelAndView) {
        LOGGER.info("SetFolderNameSetNameDescriptionDto from form {}", setDto);
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", setDto);
            modelAndView.addObject("setDto", setDto);
            modelAndView.setViewName("setCreate");
            return modelAndView;
        }

        String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        SetOfCards savedSetOfCards = setOfCardsService.saveSetFolderNameSetNameDescriptionDto(userId, setDto); // todo delete get savedSetOfCards after checking work
        LOGGER.info("set saved {}", savedSetOfCards);

        String red = "redirect:/set/" + savedSetOfCards.getId();
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);
        return modelAndView;
    }

    @GetMapping("/set/create/{id}")  // with fill folder name by setId
    public ModelAndView getSetForm(@PathVariable("id") String id,
                                   ModelAndView modelAndView) {
        LOGGER.info("invoked");

        SetFolderNameSetNameDescriptionDto setDto = new SetFolderNameSetNameDescriptionDto(); // todo get DTO like in Card
        LOGGER.info("new SetFolderNameSetNameDescriptionDto {}", setDto);

        String folderName = folderService.getNameById(id);
        LOGGER.info("folderName getNameById {}", folderName);

        setDto.setFolderName(folderName);

        modelAndView.addObject("setDto", setDto);

        modelAndView.setViewName("setCreate");
        LOGGER.info("before show setCreate");
        return modelAndView;
    }

    @GetMapping("/set/{id}")
    public ModelAndView getSetById(@PathVariable("id") String id,
                                   ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);

        SetViewByIdDto setViewByIdDto = setOfCardsService.getSetViewByIdDto(id);

        LOGGER.info("setViewByIdDto: {}", setViewByIdDto);
        modelAndView.addObject("setViewByIdDto", setViewByIdDto);

        List<CardIdQuestionDto> cards = cardService.getBySetId(id);
        LOGGER.info("List<CardIdQuestionDto>: {}", cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setById");
        LOGGER.info("before show setById.html");
        return modelAndView;
    }


    @GetMapping("/set/{id}/edit")
    public ModelAndView getSetEditForm(@PathVariable("id") String id,
                                       ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);

        final SetEditDto setEditDto = setOfCardsService.getSetEditDto(id);
        LOGGER.info("setEditDto: {}", setEditDto);
        modelAndView.addObject("setEditDto", setEditDto);

        modelAndView.setViewName("setEdit");
        LOGGER.info("before show setEdit.html");
        return modelAndView;
    }

    @PutMapping("/set/{id}/edit")
    public ModelAndView editSet(@ModelAttribute SetEditDto setEditDto,
                                Principal principal,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        LOGGER.info("setEditDto from form: {}", setEditDto);
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", setEditDto);
            modelAndView.addObject("setEditDto", setEditDto);
            modelAndView.setViewName("setEdit");
            return modelAndView;
        }

        final String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        SetOfCards savedSetOfCards = setOfCardsService.updateSetOfCardsBySetEditDto(userId, setEditDto);
        LOGGER.info("savedSetOfCards: {}", savedSetOfCards);

        String red = "redirect:/set/" + setEditDto.getId();
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);

        return modelAndView;
    }

    @GetMapping("/set/{id}/delete")
    public ModelAndView getSetDeleteForm(@PathVariable("id") String id,
                                         ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);

        final SetEditDto setEditDto = setOfCardsService.getSetEditDto(id);
        LOGGER.info("setEditDto: {}", setEditDto);
        modelAndView.addObject("setEditDto", setEditDto);

        List<CardIdQuestionDto> cards = cardService.getBySetId(id); // todo get only cardQuestion and add it to DTO for delete
        LOGGER.info("List<CardIdQuestionDto>: {}", cards);
        modelAndView.addObject("cards", cards);

        modelAndView.setViewName("setDeleteById");
        LOGGER.info("before show setDeleteById.html");
        return modelAndView;
    }

    @DeleteMapping("/set/{id}/delete")
    public ModelAndView deleteSet(@PathVariable("id") String id,
                                  ModelAndView modelAndView) {
        LOGGER.info("set id from link: {}", id);

        setOfCardsService.deleteAllById(id);
        LOGGER.info("setOfCards is deleted");

        modelAndView.setViewName("redirect:/folder");
        LOGGER.info("before redirect:/folder");
        return modelAndView;
    }


}
