package com.petrenko.flashcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

//@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {
//    @Bean
//    public LocaleResolver localeResolver() {
//        final CookieLocaleResolver resolver = new CookieLocaleResolver();
//        resolver.setDefaultLocale(Locale.ENGLISH);
//        resolver.setCookieName("language");
//        return resolver;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }
//
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        return new LocaleChangeInterceptor();
//    }
}