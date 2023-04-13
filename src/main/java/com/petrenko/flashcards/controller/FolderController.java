package com.petrenko.flashcards.controller;

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
    public ModelAndView getAllFolders(Principal principal, ModelAndView modelAndView) {
        LOGGER.info("invoked");

        String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        List<Folder> folders = folderService.getFoldersByPersonId(userId);
        LOGGER.info("List<Folder> ByPersonId {}", folders);
        modelAndView.addObject("folders", folders);

        modelAndView.setViewName("allFolders");
        LOGGER.info("before show allFolders.html");

        return modelAndView;
    }

    @GetMapping("/folder/{id}")
    public ModelAndView getFolderById(@PathVariable("id") String id, ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/folder/{id}) id: " + id);
        final Folder folder = folderService.getById(id);
        LOGGER.info("@GetMapping(/folder/{id}) folder getById(id): " + folder);
        modelAndView.addObject("folder", folder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        LOGGER.info("@GetMapping(/folder/{id}) List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderViewById");
        LOGGER.info("@GetMapping(/folder/{id}) before show folderViewById.html");

        return modelAndView;
    }

    @GetMapping("/folder/create")
    public ModelAndView getFolderForm(ModelAndView modelAndView) {
        LOGGER.info("@GetMapping(/folder/create)");
        Folder folder = new Folder();
        LOGGER.info("@GetMapping(/folder/create) new Folder " + folder);
        modelAndView.addObject("folder", folder);
        modelAndView.setViewName("createFolderView");
        LOGGER.info("@GetMapping(/folder/create) before show createFolderView.html");
        return modelAndView;
    }

    @PostMapping("/folder/create")  // after created folder
    public ModelAndView saveNewFolder(@ModelAttribute Folder folder,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView) {
        LOGGER.info("@PostMapping(/folder/create) " + folder);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("folder", folder);
            modelAndView.setViewName("createFolderView");
            return modelAndView;
        }

        Folder savedFolder = folderService.save(folder);
        LOGGER.info("@PostMapping(/folder/create) savedFolder " + savedFolder);
        modelAndView.addObject("folder", savedFolder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        LOGGER.info("@PostMapping(/folder/create)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderViewById");
        LOGGER.info("@PostMapping(/folder/create) before show folderViewById.html");
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

        modelAndView.setViewName("folderViewById");
        LOGGER.info("@PutMapping(/folder/{id}/edit) before show folderViewById.html");
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
        modelAndView.setViewName("createFolderView");
        LOGGER.info("@DeleteMapping(/folder/{id}/delete) before show createFolderView.html");
        return modelAndView;

    }


}
