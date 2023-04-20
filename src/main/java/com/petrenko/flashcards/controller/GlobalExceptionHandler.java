package com.petrenko.flashcards.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        LOGGER.info("invoked {}", ex.getStackTrace());
        ModelAndView modelAndView = new ModelAndView();

        String message;
        if (ex.getMessage() == null) {
            message = "Something went wrong";
        } else {
            message = ex.getMessage();
        }
        modelAndView.setViewName("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }

}
