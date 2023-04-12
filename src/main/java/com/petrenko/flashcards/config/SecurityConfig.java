package com.petrenko.flashcards.config;

import com.petrenko.flashcards.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PersonService personService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PersonService personService, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration", "/", "/*/*.css").permitAll()
                .anyRequest().authenticated()
                .and()
//                .formLogin(f -> f
//                        .loginPage("/signIn.html")
//                        .permitAll())
                .formLogin().permitAll()
                .and()
                .rememberMe()
                .and()
                .logout().permitAll()
                .invalidateHttpSession(true)
                .and()
                .csrf().disable();

//                        .formLogin(f -> f
//                        .loginPage("/signin")
//                        .permitAll()
//                )
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personService)
                .passwordEncoder(passwordEncoder);
    }

}
