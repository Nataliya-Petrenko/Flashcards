package com.petrenko.flashcards.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping()
public class OtherController {
    private final static Logger LOGGER = LoggerFactory.getLogger(OtherController.class);

    @GetMapping("/info")
    public ModelAndView getInfo(ModelAndView modelAndView) {
        modelAndView.setViewName("info");
        return modelAndView;
    }

}
