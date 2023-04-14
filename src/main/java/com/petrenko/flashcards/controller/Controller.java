package com.petrenko.flashcards.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
@RequestMapping()
public class Controller {
    private final static Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/")
    public ModelAndView getSearch(ModelAndView modelAndView) {
        LOGGER.info("invoked");
        modelAndView.setViewName("search"); // todo search!
        return modelAndView;
    }

}
