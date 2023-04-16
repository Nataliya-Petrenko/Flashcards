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

@Service
public class PersonService implements UserDetailsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(final PersonRepository personRepository,
                         final PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        LOGGER.info("invoked");
        return personRepository.findPersonByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Person getById(final String userId) {
        LOGGER.info("invoked");
        final Person person = personRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found. User ID: " + userId));
        LOGGER.info("Person findById {}", person);
        return person;
    }

    public Person saveRegistrationPersonDtoToPerson(final RegistrationPersonDto user) {
        LOGGER.info("invoked");
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password is incorrect");
        }

        if (personRepository.findPersonByEmail(user.getEmail()).isPresent()) {
            LOGGER.info("User already exists {}", user.getEmail());
            throw new IllegalArgumentException("User with this email already exists: " + user.getEmail());
        }
        final Person person = new Person();

        person.setPassword(passwordEncoder.encode(user.getPassword()));
        person.setEmail(user.getEmail());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());

        final long countOfPerson = personRepository.count();
        LOGGER.info("countOfPerson {}", countOfPerson);
        if (countOfPerson == 0) {
            LOGGER.info("first user");
            person.setRole(Role.ADMIN);
        } else {
            person.setRole(Role.USER);
        }

        final Person savedPerson = personRepository.save(person);
        LOGGER.info("savedPerson {}", savedPerson);
        return savedPerson;
    }

    public EditProfileDto getEditProfileDtoByUserId(final String userId) {
        LOGGER.info("invoked");
        final EditProfileDto editProfileDto = personRepository.getEditProfileDtoByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User information for editing not found. User ID: " + userId));
        LOGGER.info("editProfileDto {}", editProfileDto);
        return editProfileDto;
    }

    @Transactional
    public Person updatePersonFromEditProfileDto(final String userId, final EditProfileDto editProfileDto) {
        LOGGER.info("invoked");

        final String newEmail = editProfileDto.getEmail();
        final String newFirstName = editProfileDto.getFirstName();
        final String newLastName = editProfileDto.getLastName();

        personRepository.update(userId, newEmail, newFirstName, newLastName);

        final Person editedPerson = getById(userId);
        LOGGER.info("editedPerson {}", editedPerson);

        return editedPerson;
    }

    public List<UsersInfoDto> getAll() {
        LOGGER.info("invoked");
        final List<UsersInfoDto> users = personRepository.getUsersInfoDto();
        LOGGER.info("users {}", users);
        return users;
    }

    public UsersInfoDto getUsersInfoDto(final String userId) {
        LOGGER.info("invoked");
        final UsersInfoDto user = personRepository.getUserInfoDto(userId)
                .orElseThrow(() -> new IllegalArgumentException("User information not found. User ID: " + userId));

        LOGGER.info("user {}", user);
        return user;
    }

    @Transactional
    public void turnBlockingUser(final String userId) {
        LOGGER.info("invoked");
        final boolean enable = personRepository.getEnable(userId);
        if (enable) {
            blockUser(userId);
        } else {
            unblockUser(userId);
        }
    }

    private void blockUser(final String userId) {
        LOGGER.info("invoked");
        personRepository.blockUser(userId, false);
    }

    private void unblockUser(final String userId) {
        LOGGER.info("invoked");
        personRepository.blockUser(userId, true);
    }

    public List<UsersInfoDto> getBySearch(final String search) { // todo: not dependents from case
        LOGGER.info("invoked");
        final List<UsersInfoDto> users = personRepository.getBySearch(search);
        LOGGER.info("users {}", users);
        return users;
    }
}
