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
        LOGGER.trace("invoked for username '{}'", username);
        return personRepository.findPersonByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Person getById(final String userId) {
        LOGGER.trace("invoked for userId '{}'", userId);
        final Person person = personRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found. User ID: " + userId));
        LOGGER.info("Person findById {}", person);
        return person;
    }

    public Person saveRegistrationPersonDtoToPerson(final RegistrationPersonDto user) {
        LOGGER.trace("invoked for RegistrationPersonDto '{}'", user);
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
        LOGGER.trace("countOfPerson {}", countOfPerson);
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
        LOGGER.trace("invoked for userId '{}'", userId);
        final EditProfileDto editProfileDto = personRepository.getEditProfileDtoByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User information for editing not found. User ID: " + userId));
        LOGGER.info("editProfileDto {}", editProfileDto);
        return editProfileDto;
    }

    @Transactional
    public Person updatePersonFromEditProfileDto(final String userId, final EditProfileDto editProfileDto) {
        LOGGER.trace("invoked for userId '{}' and editProfileDto '{}'", userId, editProfileDto);

        final String newEmail = editProfileDto.getEmail();
        final String newFirstName = editProfileDto.getFirstName();
        final String newLastName = editProfileDto.getLastName();

        personRepository.update(userId, newEmail, newFirstName, newLastName);

        final Person editedPerson = getById(userId);
        LOGGER.info("editedPerson {}", editedPerson);

        return editedPerson;
    }

    public List<UsersInfoDto> getAll() {
        final List<UsersInfoDto> users = personRepository.getUsersInfoDto();
        LOGGER.info("users {}", users);
        return users;
    }

    public UsersInfoDto getUsersInfoDto(final String userId) {
        LOGGER.trace("invoked for userId '{}'", userId);
        final UsersInfoDto user = personRepository.getUserInfoDto(userId)
                .orElseThrow(() -> new IllegalArgumentException("User information not found. User ID: " + userId));

        LOGGER.info("user {}", user);
        return user;
    }

    @Transactional
    public void turnBlockingUser(final String userId) {
        LOGGER.trace("invoked for userId '{}'", userId);
        final boolean enable = personRepository.getEnable(userId);
        if (enable) {
            blockUser(userId);
        } else {
            unblockUser(userId);
        }
    }

    private void blockUser(final String userId) {
        personRepository.blockUser(userId, false);
        LOGGER.trace("userId '{}' is blocked", userId);
    }

    private void unblockUser(final String userId) {
        personRepository.blockUser(userId, true);
        LOGGER.trace("userId '{}' is unblocked", userId);
    }

    @Transactional
    public void changeRoleUser(final String userId) {
        LOGGER.trace("invoked for userId '{}'", userId);
        String role = personRepository.getRole(userId);
        if (Role.USER.equals(Role.valueOf(role))) {
            makeAdmin(userId);
        } else {
            makeUser(userId);
        }
    }

    public void makeAdmin(String userId) {
        personRepository.updateRole(userId, Role.ADMIN);
        LOGGER.trace("userId '{}' is admin", userId);
    }

    public void makeUser(String userId) {
        personRepository.updateRole(userId, Role.USER);
        LOGGER.trace("userId '{}' is user", userId);
    }

    public List<UsersInfoDto> getBySearch(final String search) {
        final List<UsersInfoDto> users = personRepository.getBySearch(search);
        LOGGER.info("for search '{}': users {}", search, users);
        return users;
    }

}
