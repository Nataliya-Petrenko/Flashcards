package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.RegistrationPersonDto;
import com.petrenko.flashcards.dto.UsersInfoDto;
import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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
        final RegistrationPersonDto registrationPersonDto = new RegistrationPersonDto();
        modelAndView.addObject("registrationPersonDto", registrationPersonDto);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView saveNewPerson(@ModelAttribute @Valid RegistrationPersonDto registrationPersonDto,
                                      BindingResult bindingResult,
                                      ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", registrationPersonDto);
            modelAndView.addObject("registrationPersonDto", registrationPersonDto);
            modelAndView.setViewName("registration");
            return modelAndView;
        }
        final Person savedPerson = personService.saveRegistrationPersonDtoToPerson(registrationPersonDto);
        modelAndView.setViewName("redirect:/folder");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView getProfileForm(ModelAndView modelAndView,
                                       Principal principal) {
        String userId = principal.getName();
        LOGGER.trace("userId {}", userId);

        final EditProfileDto editProfileDto = personService.getEditProfileDtoByUserId(userId);
        modelAndView.addObject("editProfileDto", editProfileDto);

        modelAndView.setViewName("profileEdit");
        return modelAndView;
    }

    @PutMapping("/profile/edit")
    public ModelAndView editProfile(@ModelAttribute EditProfileDto editProfileDto,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView,
                                    Principal principal) {
        LOGGER.trace("editProfileDto from form {}", editProfileDto);
        if (bindingResult.hasErrors()) {
            LOGGER.error("return with input error {}", editProfileDto);
            modelAndView.addObject("editProfileDto", editProfileDto);
            modelAndView.setViewName("profileEdit");
            return modelAndView;
        }

        final String userId = principal.getName();
        personService.updatePersonFromEditProfileDto(userId, editProfileDto);

        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public ModelAndView getAllUsers(ModelAndView modelAndView) {
        final List<UsersInfoDto> users = personService.getAll();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{id}")
    public ModelAndView getUser(@PathVariable("id") String id,
                                ModelAndView modelAndView) {
        LOGGER.info("invoked with id: {}", id);
        final UsersInfoDto user = personService.getUsersInfoDto(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userInfo");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user/{id}/block")
    public ModelAndView makeBlock(@PathVariable("id") String id,
                                 ModelAndView modelAndView) {
        LOGGER.info("invoked with id: {}", id);
        personService.turnBlockingUser(id);
        modelAndView.setViewName("redirect:/user/" + id);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user/{id}/role")
    public ModelAndView makeAdmin(@PathVariable("id") String id,
                                  ModelAndView modelAndView) {
        LOGGER.info("invoked with id: {}", id);
        personService.changeRoleUser(id);
        modelAndView.setViewName("redirect:/user/" + id);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user")
    public ModelAndView searchUser(@RequestParam("search") String search,
                                   ModelAndView modelAndView) {
        final List<UsersInfoDto> users = personService.getBySearch(search);
        modelAndView.addObject("users", users);
        modelAndView.setViewName("users");
        return modelAndView;
    }

}
