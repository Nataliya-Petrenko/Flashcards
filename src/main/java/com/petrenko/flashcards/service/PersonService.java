package com.petrenko.flashcards.service;

import com.petrenko.flashcards.dto.EditProfileDto;
import com.petrenko.flashcards.dto.RegistrationPersonDto;
import com.petrenko.flashcards.dto.UsersInfoDto;
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

import java.util.List;
import java.util.Optional;


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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("invoked");
        return personRepository.findPersonByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Person getById(String userId) {
        LOGGER.info("invoked");
        Person person = personRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found. User ID: " + userId));
        LOGGER.info("Person findById {}", person);
        return person;
    }

    public Person saveRegistrationPersonDtoToPerson(RegistrationPersonDto user) {
        LOGGER.info("invoked");
        if (user.getPassword() == null) {               //todo Do I need it if I have validation?
            throw new IllegalArgumentException("Password is incorrect");
        }

        if (personRepository.findPersonByEmail(user.getEmail()).isPresent()) {
            LOGGER.info("User already exists {}", user.getEmail());
            throw new IllegalArgumentException("User with this email already exists: " + user.getEmail());
        }
        Person person = new Person();

        person.setPassword(passwordEncoder.encode(user.getPassword()));
        person.setEmail(user.getEmail());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());

        long countOfPerson = personRepository.count();
        LOGGER.info("countOfPerson {}", countOfPerson);
        if (countOfPerson == 0) {
            LOGGER.info("first user");
            person.setRole(Role.ADMIN);
        } else {
            person.setRole(Role.USER);
        }

        Person savedPerson = personRepository.save(person);
        LOGGER.info("savedPerson {}", savedPerson);
        return savedPerson;
    }

    public EditProfileDto getEditProfileDtoByUserId(String userId) {
        LOGGER.info("invoked");
        EditProfileDto editProfileDto = personRepository.getEditProfileDtoByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User information for editing not found. User ID: " + userId));
        LOGGER.info("editProfileDto {}", editProfileDto);
        return editProfileDto;
    }

    @Transactional
    public Person updatePersonFromEditProfileDto(String userId, EditProfileDto editProfileDto) {
        LOGGER.info("invoked");

        String newEmail = editProfileDto.getEmail();
        String newFirstName = editProfileDto.getFirstName();
        String newLastName = editProfileDto.getLastName();
        String newAvatar = editProfileDto.getAvatar();

        personRepository.update(userId, newEmail, newFirstName, newLastName, newAvatar);

        Person editedPerson = getById(userId);
        LOGGER.info("editedPerson {}", editedPerson);

        return editedPerson;
    }

    public List<UsersInfoDto> getAll() {
        LOGGER.info("invoked");
        List<UsersInfoDto> users = personRepository.getUsersInfoDto();
        LOGGER.info("users {}", users);
        return users;
    }

    public UsersInfoDto getUsersInfoDto(String userId) {
        LOGGER.info("invoked");
        UsersInfoDto user = personRepository.getUserInfoDto(userId)
                .orElseThrow(() -> new IllegalArgumentException("User information not found. User ID: " + userId));

        LOGGER.info("user {}", user);
        return user;
    }

    @Transactional
    public void turnBlockingUser(String userId) {
        LOGGER.info("invoked");
        boolean enable = personRepository.getEnable(userId);
        if (enable) {
            blockUser(userId);
        } else {
            unblockUser(userId);
        }
    }

    private void blockUser(String userId) {
        LOGGER.info("invoked");
        personRepository.blockUser(userId, false);
    }

    private void unblockUser(String userId) {
        LOGGER.info("invoked");
        personRepository.blockUser(userId, true);
    }

    public List<UsersInfoDto> getBySearch(String search) { // todo: not dependents from case
        LOGGER.info("invoked");
        List<UsersInfoDto> users = personRepository.getBySearch(search);
        LOGGER.info("users {}", users);
        return users;
    }
}
