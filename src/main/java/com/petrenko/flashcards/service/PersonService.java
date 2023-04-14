package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.RegistrationPersonDto;
import com.petrenko.flashcards.model.Person;
import com.petrenko.flashcards.model.Role;
import com.petrenko.flashcards.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PersonService implements UserDetailsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // todo I need it?
        LOGGER.info("invoked");
        return personRepository.findPersonByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Person getById(String userId) {
        LOGGER.info("invoked");
        Person person = personRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        LOGGER.info("Person findById {}", person);
        return person;
    }

    public Person saveRegistrationPersonDtoToPerson(RegistrationPersonDto user) {
        LOGGER.info("invoked");
//        if (user.getPassword() == null) {               //todo Do I need it if I have validation?
//            throw new IllegalArgumentException("Password is incorrect");
//        }
        if (personRepository.findPersonByEmail(user.getEmail()).isPresent()) {
            LOGGER.info("User already exists {}", user.getEmail());
            throw new IllegalArgumentException("User already exists");
        }
        Person person = new Person();

        person.setPassword(passwordEncoder.encode(user.getPassword()));
        person.setEmail(user.getEmail());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());
        person.setRole(Role.USER);
//        person.setRole(user.getEmail().equalsIgnoreCase("admin") ? Role.ADMIN : Role.USER); // todo another approach get role

        Person savedPerson = personRepository.save(person);
        LOGGER.info("savedPerson {}", savedPerson);
        return savedPerson;
    }

    public EditProfileDto getEditProfileDtoByUserId(String userId) {
        LOGGER.info("invoked");
        EditProfileDto editProfileDto = personRepository.getEditProfileDtoByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
        LOGGER.info("editProfileDto {}", editProfileDto);
        return editProfileDto;
    }

    @Transactional
    public Person updatePersonFromEditProfileDto(String userId, EditProfileDto editProfileDto) {
        LOGGER.info("invoked");

        String newEmail = editProfileDto.getEmail();
        String newFirstName = editProfileDto.getFirstName();
        String newLastName = editProfileDto.getLastName();

//        if (!editProfileDto.getPassword().isBlank()) { // todo own page for password to add required for password pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" in form
//            String newPassword = editProfileDto.getPassword();
//            personRepository.updatePersonFromEditProfileDto(userId, newEmail, newFirstName, newLastName, newPassword);
//        }
        personRepository.update(userId, newEmail, newFirstName, newLastName);

        Person editedPerson = getById(userId);
        LOGGER.info("editedPerson {}", editedPerson);

        return editedPerson;
    }

    // todo admin can block user (enable=false)
}
