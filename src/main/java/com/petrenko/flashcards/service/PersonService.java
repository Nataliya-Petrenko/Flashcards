package com.petrenko.flashcards.service;

import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.model.Role;
import com.petrenko.flashcards.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findPersonByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void save(Person user) {
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        if (personRepository.findPersonByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getEmail().equalsIgnoreCase("admin") ? Role.ADMIN : Role.USER); // todo another approach get role
        personRepository.save(user);
    }

    // todo admin can block user (enable=false)
}
