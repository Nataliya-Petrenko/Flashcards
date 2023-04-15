package com.petrenko.flashcards.controller;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.RegistrationPersonDto;
import com.petrenko.flashcards.dto.UsersInfoDto;
import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//                                    @RequestParam("avatar") MultipartFile avatarFile,
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

//        if (!editProfileDto.getAvatar().isEmpty()) {
//            // todo Save the file to disk or process the file in some other way
//        }

        String userId = principal.getName();
        Person savedPerson = personService.updatePersonFromEditProfileDto(userId, editProfileDto);
        LOGGER.info("savedPerson {}", savedPerson);

        modelAndView.setViewName("redirect:/");
        LOGGER.info("before redirect:/");
        return modelAndView;
    }

    //todo personInfo for header or link to profile instead of info

    @GetMapping("/user")     // todo information about role is not showed
    public ModelAndView getAllUsers(ModelAndView modelAndView) {
        LOGGER.info("invoked");

        List<UsersInfoDto> users = personService.getAll();

        LOGGER.info("users {}", users);
        modelAndView.addObject("users", users);

        modelAndView.setViewName("users");
        LOGGER.info("before show users.html");
        return modelAndView;
    }

    @GetMapping("/user/{id}")
    public ModelAndView getUser(@PathVariable("id") String id,
                                ModelAndView modelAndView) {
        LOGGER.info("invoked");

        UsersInfoDto user = personService.getUsersInfoDto(id);

        LOGGER.info("user {}", user);
        modelAndView.addObject("user", user);

        modelAndView.setViewName("userInfo");
        LOGGER.info("before show userInfo.html");
        return modelAndView;
    }

//        @GetMapping("/card/{id}")
//    public ModelAndView getCardById(@PathVariable("id") String id,
//                                    ModelAndView modelAndView) {
//        LOGGER.info("card id from link: {}", id);
//
//        CardByIdDto cardByIdDto = cardService.getCardByIdDto(id);
//        modelAndView.addObject("card", cardByIdDto);
//        LOGGER.info("cardByIdDto: {}", cardByIdDto);
//
//        modelAndView.setViewName("cardById");
//        return modelAndView;
//    }

    // todo search userById
    // todo add button for admin (show users) if role ADMIN


    // todo custom sign-in

}
