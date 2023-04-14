package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.CardEditingDto;
import com.petrenko.flashcards.dto.CardIdQuestionDto;
import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository<Card, String> {
    List<Card> getBySetOfCards(SetOfCards setOfCards);

    // todo next 4 should have depending on set and folder
    @Query("""
            SELECT c.id
            FROM Card c
            WHERE c.timeOfCreation = (
              SELECT MAX(c2.timeOfCreation)
              FROM Card c2
              WHERE c2.timeOfCreation < (
                SELECT c3.timeOfCreation
                FROM Card c3
                WHERE c3.id = :currentId
              )
            )
            """)
    Optional<String> getPreviousCardId(@Param("currentId") String id); // work (the oldest get null)

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
                    LEFT JOIN s.folder f
                    LEFT JOIN f.person p
                WHERE c3.id = :cardId AND p.id = :userId AND s.id = c3.setOfCards.id AND f.id = c3.setOfCards.folder.id
              )
            )
            """)
    Optional<String> getPreviousCardIdNew(String userId, String cardId); // work (the oldest get null) todo not work -- it's time to create DTO

    @Query("""
            SELECT c.id
            FROM Card c
            WHERE c.timeOfCreation = (
              SELECT MIN(c2.timeOfCreation)
              FROM Card c2
              WHERE c2.timeOfCreation > (
                SELECT c3.timeOfCreation
                FROM Card c3
                WHERE c3.id = :currentId
              )
            )
            """)
    Optional<String> getNextCardId(@Param("currentId") String id);  // work (the newest get null)

    @Query("""
            SELECT id
            FROM Card
            WHERE timeOfCreation = (
            SELECT MIN(timeOfCreation)
            From Card
            )
                 """)
    Optional<String> getFirstCardId(String id); // work

    @Query("""
            SELECT id
            FROM Card
            WHERE timeOfCreation = (
            SELECT MAX(timeOfCreation)
            From Card
            )
                 """)
    Optional<String> getLastCardId(String id); // work

    @Query("""
            SELECT new com.petrenko.flashcards.dto.CardEditingDto(
            c.id,
            c.question,
            c.shortAnswer,
            c.longAnswer,
            s.id as setOfCardsId,
            s.name as setOfCardsName)
            FROM Card as c
            LEFT JOIN c.setOfCards as s
            WHERE c.id = :id
            """)
    Optional<CardEditingDto> getCardEditingDto(@Param("id") String id);

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
}
