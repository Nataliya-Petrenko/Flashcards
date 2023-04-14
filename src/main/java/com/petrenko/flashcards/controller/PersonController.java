package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.RegistrationPersonDto;
import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
        RegistrationPersonDto registrationPersonDto = new RegistrationPersonDto();
        modelAndView.addObject("registrationPersonDto", registrationPersonDto);
        LOGGER.info("new RegistrationPersonDto() {}", registrationPersonDto);
        modelAndView.setViewName("registration");
        LOGGER.info("before show registration.html");
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView saveNewPerson(@ModelAttribute @Valid RegistrationPersonDto registrationPersonDto,
                                      BindingResult bindingResult,
                                      ModelAndView modelAndView) {
        LOGGER.info("invoked");
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", registrationPersonDto);
            modelAndView.addObject("registrationPersonDto", registrationPersonDto);
            modelAndView.setViewName("registration");
            return modelAndView;
        }
        Person savedPerson = personService.saveRegistrationPersonDtoToPerson(registrationPersonDto);
        LOGGER.info("savedPerson {}", savedPerson);

        modelAndView.setViewName("redirect:/folder");
        LOGGER.info("before redirect:/folder");
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView getProfileForm(ModelAndView modelAndView,
                                       Principal principal) {
        LOGGER.info("invoked");
        String userId = principal.getName();
        LOGGER.info("userId {}", userId);

        EditProfileDto editProfileDto = personService.getEditProfileDtoByUserId(userId);
        LOGGER.info("editProfileDto {}", editProfileDto);
        modelAndView.addObject("editProfileDto", editProfileDto);

        modelAndView.setViewName("profileEdit");
        LOGGER.info("before show profileEdit.html");
        return modelAndView;
    }

    @PutMapping("/profile/edit")
    public ModelAndView editProfile(@ModelAttribute EditProfileDto editProfileDto,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView,
                                    Principal principal) {
        LOGGER.info("editProfileDto from form {}", editProfileDto);
        if (bindingResult.hasErrors()) {
            LOGGER.info("return with input error {}", editProfileDto);
            modelAndView.addObject("editProfileDto", editProfileDto);
            modelAndView.setViewName("profileEdit");
            return modelAndView;
        }

        String userId = principal.getName();
        Person savedPerson = personService.updatePersonFromEditProfileDto(userId, editProfileDto);
        LOGGER.info("savedPerson {}", savedPerson);

        modelAndView.setViewName("redirect:/");
        LOGGER.info("before redirect:/");
        return modelAndView;
    }

    //todo personInfo for header or link to profile instead of info


}
