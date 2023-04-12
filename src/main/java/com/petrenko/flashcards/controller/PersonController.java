package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping()
public class PersonController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationForm(ModelAndView modelAndView) {
        LOGGER.info("invoked");
        modelAndView.addObject("user", new Person());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView saveNewPerson(@ModelAttribute Person user, ModelAndView modelAndView) {
        LOGGER.info("invoked");
        personService.save(user);
        modelAndView.setViewName("redirect:/card/create");
        return modelAndView;
    }



//    @GetMapping("/signin")
//    public ModelAndView getSigninForm(ModelAndView modelAndView) {
//        LOGGER.info("invoked");
////        modelAndView.addObject("user", new Person());
//        modelAndView.setViewName("signIn");
//        return modelAndView;
//    }

//    @PostMapping("/registration")
//    public ModelAndView saveNewPerson(@ModelAttribute Person user, ModelAndView modelAndView) {
//        LOGGER.info("invoked");
//        personService.save(user);
//        modelAndView.setViewName("redirect:/card/create");
//        return modelAndView;
//    }

}
