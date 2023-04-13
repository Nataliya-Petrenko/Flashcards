package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

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
        Person person = new Person();
        modelAndView.addObject("user", person);
        LOGGER.info("new Person() {}", person);
        modelAndView.setViewName("registration");
        LOGGER.info("before show registration.html");
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView saveNewPerson(@ModelAttribute Person user, ModelAndView modelAndView) {
        LOGGER.info("invoked");
        Person savedPerson = personService.save(user);
        LOGGER.info("savedPerson {}", savedPerson);
        modelAndView.setViewName("redirect:/profile");
        LOGGER.info("before redirect:/profile");
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView getProfileForm(ModelAndView modelAndView,
                                   Principal principal) {
        LOGGER.info("invoked");
        String userId = principal.getName();
        Person personByName = personService.getById(userId);
        modelAndView.addObject("person", personByName);

        String newPassword = "";
        modelAndView.addObject("newPassword", newPassword);

        modelAndView.setViewName("profileEdit");
        LOGGER.info("before show profileEdit.html");
        return modelAndView;
    }

    @PutMapping("/profile/edit")
    public ModelAndView editProfile(@ModelAttribute Person person,
                                   @ModelAttribute String newPassword, // todo I'm not sure it is good approach
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView,
                                   Principal principal) {
        LOGGER.info("person from form {}", person);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("person", person);
            modelAndView.setViewName("profileEdit");
            return modelAndView;
        }

        person.setPassword(newPassword);
        String userId = principal.getName();
        Person savedPerson = personService.edit(person, userId);
        LOGGER.info("savedPerson {}", savedPerson);

        modelAndView.setViewName("redirect:/");
        LOGGER.info("before redirect:/");
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
