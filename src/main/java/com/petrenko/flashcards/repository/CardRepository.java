package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.*;
import com.petrenko.flashcards.model.Card;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository<Card, String> {

    @Modifying
    @Query("""
            DELETE
            FROM Card c
            WHERE c.setOfCards.id = :setId
            """)
    void deleteBySetId(String setId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.CardIdQuestionDto(
            c.id,
            c.question)
            FROM Card as c
            LEFT JOIN c.setOfCards as s
            WHERE s.id = :setId
            """)
    List<CardIdQuestionDto> findBySetOfCardsId(String setId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.CardByIdDto(
            c.id,
            c.question,
            c.shortAnswer,
            c.longAnswer,
            s.id,
            s.name,
            f.name)
            FROM Card as c
            LEFT JOIN c.setOfCards as s
            LEFT JOIN s.folder as f
            WHERE c.id = :cardId
            """)
    Optional<CardByIdDto> getCardByIdDto(String cardId);


    @Query("""
            SELECT c.id
            FROM Card c
            WHERE c.timeOfCreation = (
              SELECT MAX(c2.timeOfCreation)
              FROM Card c2
              WHERE c2.timeOfCreation < (
                SELECT c3.timeOfCreation
                FROM Card c3
                LEFT JOIN c3.setOfCards s
                WHERE s.id = :setId AND c3.id = :cardId
              )
            )
            """)
    Optional<String> getPreviousId(String setId, String cardId); // the oldest get null

    @Query("""
            SELECT c.id
            FROM Card c
            WHERE c.timeOfCreation = (
                SELECT MAX(c.timeOfCreation)
                From Card c
                LEFT JOIN c.setOfCards s
                WHERE s.id = :setId
                )
                 """)
    Optional<String> getLastId(String setId);

    @Query("""
            SELECT c.id
            FROM Card c
            WHERE c.timeOfCreation = (
              SELECT MIN(c2.timeOfCreation)
              FROM Card c2
              WHERE c2.timeOfCreation > (
                SELECT c3.timeOfCreation
                FROM Card c3
                LEFT JOIN c3.setOfCards s
                WHERE c3.id = :cardId AND s.id = :setId
              )
            )
            """)
    Optional<String> getNextId(String setId, String cardId);  // the newest get null

    @Query("""
            SELECT c.id
            FROM Card c
            WHERE c.timeOfCreation = (
                SELECT MIN(c.timeOfCreation)
                From Card c
                LEFT JOIN c.setOfCards s
                WHERE s.id = :setId
                )
                 """)
    Optional<String> getFirstId(String setId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.CardEditDto(
            c.id,
            c.question,
            c.shortAnswer,
            c.longAnswer,
            s.id,
            s.name,
            f.id,
            f.name)
            FROM Card as c
            LEFT JOIN c.setOfCards as s
            LEFT JOIN s.folder as f
            WHERE c.id = :cardId
            """)
    Optional<CardEditDto> getCardEditDto(String cardId);

    @Query("""
            SELECT new com.petrenko.flashcards.dto.CardIdQuestionDto(
            id,
            question)
            FROM Card
            """)
    List<CardIdQuestionDto> getAll();

    @Query("""
            SELECT new com.petrenko.flashcards.dto.CardIdQuestionDto(
            id,
            question)
            FROM Card
            WHERE question LIKE %:search%
            """)
    List<CardIdQuestionDto> getBySearch(@Param("search") String search);
}
