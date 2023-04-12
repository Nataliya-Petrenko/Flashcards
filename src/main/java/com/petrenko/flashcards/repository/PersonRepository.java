package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, String> {

    Optional<Person> findPersonByEmail(String email);
}
