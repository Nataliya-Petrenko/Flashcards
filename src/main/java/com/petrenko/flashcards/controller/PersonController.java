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
        LOGGER.info("invoked");
        final RegistrationPersonDto registrationPersonDto = new RegistrationPersonDto();
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
        final Person savedPerson = personService.saveRegistrationPersonDtoToPerson(registrationPersonDto);
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

        final EditProfileDto editProfileDto = personService.getEditProfileDtoByUserId(userId);
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

        final String userId = principal.getName();
        final Person savedPerson = personService.updatePersonFromEditProfileDto(userId, editProfileDto);
        LOGGER.info("savedPerson {}", savedPerson);

        modelAndView.setViewName("redirect:/");
        LOGGER.info("before redirect:/");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public ModelAndView getAllUsers(ModelAndView modelAndView) {
        LOGGER.info("invoked");

        final List<UsersInfoDto> users = personService.getAll();

        LOGGER.info("users {}", users);
        modelAndView.addObject("users", users);

        modelAndView.setViewName("users");
        LOGGER.info("before show users.html");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{id}")
    public ModelAndView getUser(@PathVariable("id") String id,
                                ModelAndView modelAndView) {
        LOGGER.info("invoked");

        final UsersInfoDto user = personService.getUsersInfoDto(id);

        LOGGER.info("user {}", user);
        modelAndView.addObject("user", user);

        modelAndView.setViewName("userInfo");
        LOGGER.info("before show userInfo.html");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user/{id}/block")
    public ModelAndView makeBlock(@PathVariable("id") String id,
                                 ModelAndView modelAndView) {
        LOGGER.info("invoked with id: {}", id);

        personService.turnBlockingUser(id);
        LOGGER.info("user blocked/unblocked");

        String red = "redirect:/user/" + id;
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user/{id}/role")
    public ModelAndView makeAdmin(@PathVariable("id") String id,
                                  ModelAndView modelAndView) {
        LOGGER.info("invoked with id: {}", id);

        personService.changeRoleUser(id);
        LOGGER.info("user role is turned (user/admin)");

        String red = "redirect:/user/" + id;
        modelAndView.setViewName(red);
        LOGGER.info("before {}", red);

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user")
    public ModelAndView searchUser(@RequestParam("search") String search,
                                   ModelAndView modelAndView) {
        LOGGER.info("invoked");

        final List<UsersInfoDto> users = personService.getBySearch(search);

        LOGGER.info("users {}", users);
        modelAndView.addObject("users", users);

        modelAndView.setViewName("users");
        LOGGER.info("before show users.html");
        return modelAndView;
    }



}
