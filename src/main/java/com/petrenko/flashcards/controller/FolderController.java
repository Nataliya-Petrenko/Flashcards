package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.FolderByIdDto;
import com.petrenko.flashcards.dto.FolderCreateDto;
import com.petrenko.flashcards.dto.FolderIdNameDto;
import com.petrenko.flashcards.dto.SetIdNameDto;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
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
        LOGGER.info("invoked");

        String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        List<FolderIdNameDto> foldersIdNameDto = folderService.getFoldersIdNameDtoByPersonId(userId);
        LOGGER.info("List<FolderIdNameDto> ByPersonId {}", foldersIdNameDto);
        modelAndView.addObject("foldersIdNameDto", foldersIdNameDto);

        modelAndView.setViewName("foldersAll");
        LOGGER.info("before show foldersAll.html");

        return modelAndView;
    }

    @GetMapping("/folder/create")
    public ModelAndView getFolderForm(ModelAndView modelAndView) {
        LOGGER.info("invoked");

        FolderCreateDto folderCreateDto = new FolderCreateDto();
        LOGGER.info("new folderCreateDto {}", folderCreateDto);
        modelAndView.addObject("folderCreateDto", folderCreateDto);

        modelAndView.setViewName("folderCreate");
        LOGGER.info("before show folderCreate.html");
        return modelAndView;
    }

    @PostMapping("/folder/create")
    public ModelAndView saveNewFolder(@ModelAttribute FolderCreateDto folderCreateDto,
                                      BindingResult bindingResult,
                                      Principal principal,
                                      ModelAndView modelAndView) {
        LOGGER.info("folderCreateDto from form {}", folderCreateDto);
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", folderCreateDto);
            modelAndView.addObject("folderCreateDto", folderCreateDto);
            modelAndView.setViewName("folderCreate");
            return modelAndView;
        }

        String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        Folder savedFolder = folderService.saveFolderCreateDtoToFolder(userId, folderCreateDto);
        LOGGER.info("savedFolder {}", savedFolder);

        modelAndView.setViewName("redirect:/folder");
        LOGGER.info("before redirect:/folder");

        return modelAndView;
    }

    @GetMapping("/folder/{id}")
    public ModelAndView getFolderById(@PathVariable("id") String id,
                                      Principal principal,
                                      ModelAndView modelAndView) {
        LOGGER.info("folder id from link: " + id);

        final String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        FolderByIdDto folderByIdDto = folderService.getFolderByIdDto(userId, id);
        LOGGER.info("folderByIdDto: {}", folderByIdDto);
        modelAndView.addObject("folderByIdDto", folderByIdDto);

        List<SetIdNameDto> setsIdNameDto = setOfCardsService.getByFolderId(userId, id);
        LOGGER.info("List<setsIdNameDto> getByFolderId: {}", setsIdNameDto);
        modelAndView.addObject("setsIdNameDto", setsIdNameDto);

        modelAndView.setViewName("folderById");
        LOGGER.info("before show folderById.html");
        return modelAndView;
    }

    @GetMapping("/folder/{id}/edit")
    public ModelAndView getFolderEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/folder/{id}/edit) id: " + id);
        final Folder folder = folderService.getById(id);
        LOGGER.info("@GetMapping(/folder/{id}/edit) folder getById(id): " + folder);
        modelAndView.addObject("folder", folder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        LOGGER.info("@GetMapping(/folder/{id}/edit)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("editFolderView");
        LOGGER.info("@GetMapping(/folder/{id}/edit) before show editFolderView.html");
        return modelAndView;
    }

    @PutMapping("/folder/{id}/edit")  // after edited folder
    public ModelAndView editSet(@PathVariable("id") String id,   // todo save id into Dto in view
                                @ModelAttribute Folder folder,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        LOGGER.info("@PutMapping(/folder/{id}/edit) id: " + id);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("folder", folder);
            modelAndView.setViewName("editFolderView");
            return modelAndView;
        }
        Folder savedFolder = folderService.save(folder);
        LOGGER.info("@PutMapping(/folder/{id}/edit) savedFolder " + savedFolder);
        modelAndView.addObject("folder", savedFolder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        LOGGER.info("@PutMapping(/folder/{id}/edit)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderById");
        LOGGER.info("@PutMapping(/folder/{id}/edit) before show folderById.html");
        return modelAndView;
    }

    @GetMapping("/folder/{id}/delete")
    public ModelAndView getFolderDeleteForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/folder/{id}/delete) id: " + id);
        Folder folder = folderService.getById(id);
        LOGGER.info("@GetMapping(/folder/{id}/delete) folder getById: " + folder);
        modelAndView.addObject("folder", folder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        LOGGER.info("@GetMapping(/folder/{id}/delete)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("deleteFolderViewById");
        LOGGER.info("@GetMapping(/folder/{id}/delete) before show deleteFolderViewById.html");
        return modelAndView;
    }

    @DeleteMapping("/folder/{id}/delete")  // after delete card
    public ModelAndView deleteFolder(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@DeleteMapping(/folder/{id}/delete) id: " + id);

        folderService.deleteById(id);
        LOGGER.info("@DeleteMapping(/folder/{id}/delete) folder is deleted");

// todo go to profile

        Folder folder = new Folder();
        LOGGER.info("@DeleteMapping(/folder/{id}/delete) new Folder " + folder);
        modelAndView.addObject("folder", folder);
        modelAndView.setViewName("folderCreate");
        LOGGER.info("@DeleteMapping(/folder/{id}/delete) before show folderCreate.html");
        return modelAndView;

    }


}
