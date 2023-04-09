package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.Folder;
import com.petrenko.flashcards.model.SetOfCards;
import com.petrenko.flashcards.service.CardService;
import com.petrenko.flashcards.service.FolderService;
import com.petrenko.flashcards.service.SetOfCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping()
public class FolderController {

    private final FolderService folderService;
    private final SetOfCardsService setOfCardsService;

    @Autowired
    public FolderController(final FolderService folderService,
                            final SetOfCardsService setOfCardsService) {
        this.folderService = folderService;
        this.setOfCardsService = setOfCardsService;
    }

    @GetMapping("/folder/{id}")
    public ModelAndView getFolderById(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/folder/{id}) id: " + id);
        final Folder folder = folderService.getById(id);
        System.out.println("@GetMapping(/folder/{id}) folder getById(id): " + folder);
        modelAndView.addObject("folder", folder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        System.out.println("@GetMapping(/folder/{id}) List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderViewById");
        System.out.println("@GetMapping(/folder/{id}) before show folderViewById.html");

        return modelAndView;
    }

    @GetMapping("/folder/create")
    public ModelAndView getFolderForm(ModelAndView modelAndView) {
        System.out.println("@GetMapping(/folder/create)");
        Folder folder = new Folder();
        System.out.println("@GetMapping(/folder/create) new Folder " + folder);
        modelAndView.addObject("folder", folder);
        modelAndView.setViewName("createFolderView");
        System.out.println("@GetMapping(/folder/create) before show createFolderView.html");
        return modelAndView;
    }

    @PostMapping("/folder/create")  // after created folder
    public ModelAndView saveNewFolder(@ModelAttribute Folder folder,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView) {
        System.out.println("@PostMapping(/folder/create) " + folder);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("folder", folder);
            modelAndView.setViewName("createFolderView");
            return modelAndView;
        }

        Folder savedFolder = folderService.save(folder);
        System.out.println("@PostMapping(/folder/create) savedFolder " + savedFolder);
        modelAndView.addObject("folder", savedFolder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        System.out.println("@PostMapping(/folder/create)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderViewById");
        System.out.println("@PostMapping(/folder/create) before show folderViewById.html");
        return modelAndView;
    }


    @GetMapping("/folder/{id}/edit")
    public ModelAndView getFolderEditForm(@PathVariable("id") String id, ModelAndView modelAndView) {
        System.out.println("@GetMapping(/folder/{id}/edit) id: " + id);
        final Folder folder = folderService.getById(id);
        System.out.println("@GetMapping(/folder/{id}/edit) folder getById(id): " + folder);
        modelAndView.addObject("folder", folder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        System.out.println("@GetMapping(/folder/{id}/edit)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("editFolderView");
        System.out.println("@GetMapping(/folder/{id}/edit) before show editFolderView.html");
        return modelAndView;
    }

    @PutMapping("/folder/{id}/edit")  // after edited folder
    public ModelAndView editSet(@PathVariable("id") String id,   // todo save id into Dto in view
                                @ModelAttribute Folder folder,
                                BindingResult bindingResult,
                                ModelAndView modelAndView) {
        System.out.println("@PutMapping(/folder/{id}/edit) id: " + id);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("folder", folder);
            modelAndView.setViewName("editFolderView");
            return modelAndView;
        }
        Folder savedFolder = folderService.save(folder);
        System.out.println("@PutMapping(/folder/{id}/edit) savedFolder " + savedFolder);
        modelAndView.addObject("folder", savedFolder);

        List<SetOfCards> setsOfCards = setOfCardsService.getByFolder(folder);
        System.out.println("@PutMapping(/folder/{id}/edit)  List<SetOfCards> getByFolder: " + setsOfCards);
        modelAndView.addObject("setsOfCards", setsOfCards);

        modelAndView.setViewName("folderViewById");
        System.out.println("@PutMapping(/folder/{id}/edit) before show folderViewById.html");
        return modelAndView;
    }
//
//    @GetMapping("/set/{id}/delete")
//    public ModelAndView getSetDeleteForm(@PathVariable("id") String id, ModelAndView modelAndView) {
//        System.out.println("@GetMapping(/set/{id}/delete) id: " + id);
//        final SetOfCards setOfCards = setOfCardsService.getById(id);
//        System.out.println("@GetMapping(/set/{id}/delete) setOfCards getById: " + setOfCards);
//        modelAndView.addObject("setOfCards", setOfCards);
//        modelAndView.setViewName("deleteSetViewById");
//
//        List<Card> cards = cardService.getBySet(setOfCards);
//        System.out.println("@GetMapping(/set/{id})  List<Card> getBySet: " + cards);
//        modelAndView.addObject("cards", cards);
//
//        System.out.println("@GetMapping(/set/{id}/delete) before show deleteSetViewById.html");
//        return modelAndView;
//    }
//
//    @DeleteMapping("/set/{id}/delete")  // after delete card
//    public ModelAndView deleteSet(@PathVariable("id") String id, ModelAndView modelAndView) {
//        System.out.println("@DeleteMapping(/delete/{id}) id: " + id);
//
//        // get folder and go to folder view
////        final SetOfCards setOfCards = setOfCardsService.getById(cardService.getById(id).getSetOfCards().getId());
////        System.out.println("@DeleteMapping(/delete/{id}) setOfCards getById: " + setOfCards);
////        modelAndView.addObject("setOfCards", setOfCards);
//
//        setOfCardsService.deleteById(id);
//        System.out.println("@DeleteMapping(/delete/{id}) setOfCards is deleted");
//
////        List<Card> cards = cardService.getBySet(setOfCards);
////        System.out.println("@DeleteMapping(/delete/{id}) List<Card>: " + cards);
////        modelAndView.addObject("cards", cards);
//
////        modelAndView.setViewName("setViewById");
//
//// todo go to folder or profile
//        SetOfCards setOfCards = new SetOfCards();
//        System.out.println("@DeleteMapping(/delete/{id}) new SetOfCards " + setOfCards);
//        modelAndView.addObject("setOfCards", setOfCards);
//        modelAndView.setViewName("createSetView");
//
//        System.out.println("@DeleteMapping(/delete/{id}) before show createSetView.html");
//        return modelAndView;
//    }


}
