package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.*;
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
public class FolderController {
    private final static Logger LOGGER = LoggerFactory.getLogger(FolderController.class);
    private final FolderService folderService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public FolderController(final FolderService folderService,
                            final SetOfCardsService setOfCardsService) {
        this.folderService = folderService;
        this.setOfCardsService = setOfCardsService;
    }

    @GetMapping("/folder")
    public ModelAndView getAllFolders(Principal principal,
                                      ModelAndView modelAndView) {
        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);
        final List<FolderIdNameDto> foldersIdNameDto = folderService.getFoldersIdNameDtoByPersonId(userId);
        modelAndView.addObject("foldersIdNameDto", foldersIdNameDto);
        modelAndView.setViewName("foldersAll");
        return modelAndView;
    }

    @GetMapping("/folder/create")
    public ModelAndView getFolderForm(ModelAndView modelAndView) {
        final FolderCreateDto folderCreateDto = new FolderCreateDto();
        modelAndView.addObject("folderCreateDto", folderCreateDto);
        modelAndView.setViewName("folderCreate");
        return modelAndView;
    }

    @PostMapping("/folder/create")
    public ModelAndView saveNewFolder(@ModelAttribute FolderCreateDto folderCreateDto,
                                      BindingResult bindingResult,
                                      Principal principal,
                                      ModelAndView modelAndView) {
        LOGGER.trace("folderCreateDto from form {}", folderCreateDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", folderCreateDto);
            modelAndView.addObject("folderCreateDto", folderCreateDto);
            modelAndView.setViewName("folderCreate");
            return modelAndView;
        }
        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);
        folderService.saveFolderCreateDtoToFolder(userId, folderCreateDto);
        modelAndView.setViewName("redirect:/folder");
        return modelAndView;
    }

    @GetMapping("/folder/{id}")
    public ModelAndView getFolderById(@PathVariable("id") String id,
                                      Principal principal,
                                      ModelAndView modelAndView) {
        LOGGER.trace("folder id from link: {}", id);

        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);

        final FolderByIdDto folderByIdDto = folderService.getFolderByIdDto(userId, id);
        modelAndView.addObject("folderByIdDto", folderByIdDto);

        final List<SetIdNameDto> setsIdNameDto = setOfCardsService.getByFolderId(id);
        modelAndView.addObject("setsIdNameDto", setsIdNameDto);

        modelAndView.setViewName("folderById");
        return modelAndView;
    }

    @GetMapping("/folder/{id}/edit")
    public ModelAndView getFolderEditForm(@PathVariable("id") String id,
                                          ModelAndView modelAndView) {
        LOGGER.trace("folder id from link: {}", id);

        final FolderIdNameDescriptionDto folderIdNameDescriptionDto = folderService.getFolderIdNameDescriptionDto(id);
        modelAndView.addObject("folderIdNameDescriptionDto", folderIdNameDescriptionDto);

        modelAndView.setViewName("folderEdit");
        return modelAndView;
    }

    @PutMapping("/folder/{id}/edit")
    public ModelAndView editFolder(@ModelAttribute FolderIdNameDescriptionDto folderIdNameDescriptionDto,
                                Principal principal,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        LOGGER.trace("folderIdNameDescriptionDto from form: {}", folderIdNameDescriptionDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", folderIdNameDescriptionDto);
            modelAndView.addObject("folderIdNameDescriptionDto", folderIdNameDescriptionDto);
            modelAndView.setViewName("folderEdit");
            return modelAndView;
        }

        final String userId = principal.getName();
        LOGGER.trace("userId {}", userId);

        folderService.updateFolderByFolderIdNameDescriptionDto(userId, folderIdNameDescriptionDto);

        modelAndView.setViewName("redirect:/folder/" + folderIdNameDescriptionDto.getId());
        return modelAndView;
    }

    @GetMapping("/folder/{id}/delete")
    public ModelAndView getFolderDeleteForm(@PathVariable("id") String id,
                                            ModelAndView modelAndView) {
        LOGGER.trace("folder id from link: " + id);

        final FolderIdNameDescriptionDto folderIdNameDescriptionDto = folderService.getFolderIdNameDescriptionDto(id);
        modelAndView.addObject("folderIdNameDescriptionDto", folderIdNameDescriptionDto);

        final List<SetIdNameDto> setsIdNameDto = setOfCardsService.getByFolderId(id);
        modelAndView.addObject("setsIdNameDto", setsIdNameDto);

        modelAndView.setViewName("folderDeleteById");
        return modelAndView;
    }

    @DeleteMapping("/folder/{id}/delete")
    public ModelAndView deleteFolder(@PathVariable("id") String id,
                                     ModelAndView modelAndView) {
        LOGGER.trace("folder id from link: " + id);
        folderService.deleteAllByFolderId(id);
        modelAndView.setViewName("redirect:/folder");
        return modelAndView;
    }

}
