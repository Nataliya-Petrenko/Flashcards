package com.petrenko.flashcards.repository;

import com.petrenko.flashcards.dto.CardViewByIdDto;
import com.petrenko.flashcards.dto.TestDto;
import com.petrenko.flashcards.model.Card;
import com.petrenko.flashcards.model.SetOfCards;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository<Card, String> {
    List<Card> getBySetOfCards(SetOfCards setOfCards);

//    @Query("""
//    select new PostCommentDTO(
//        p.id as id,
//        p.title as title,
//        c.review as review
//    )
//    from PostComment c
//    join c.post p
//    where p.title like :postTitle
//    order by c.id
//    """)
//List<PostCommentDTO> findCommentDTOByTitle(
//    @Param("postTitle") String postTitle
//);

//    SELECT
//  (
//    SELECT id
//    FROM card
//    WHERE time_of_creation = (
//      SELECT MAX(time_of_creation)
//      FROM card
//      WHERE time_of_creation < (
//        SELECT time_of_creation
//        FROM card
//        WHERE id = 'd4bf8094-600d-49cf-8b1d-09f6901b1c01'
//      )
//    )
//  ) AS previous_id,
//  (
//    SELECT id
//    FROM card
//    WHERE time_of_creation = (
//      SELECT MIN(time_of_creation)
//      FROM card
//      WHERE time_of_creation > (
//        SELECT time_of_creation
//        FROM card
//        WHERE id = 'd4bf8094-600d-49cf-8b1d-09f6901b1c01'
//      )
//    )
//  ) AS next_id
//FROM card
//WHERE id = 'd4bf8094-600d-49cf-8b1d-09f6901b1c01';

//    @Query("""
//            SELECT id
//            FROM card
//            WHERE time_of_creation = (
//              SELECT MAX(time_of_creation)
//              FROM card
//              WHERE time_of_creation < (
//                SELECT time_of_creation
//                FROM card
//                WHERE id = current_id
//              )
//            )
//            """)
//@Query("""
//            SELECT id
//            FROM card
//            WHERE timeOfCreation = (
//              SELECT MAX(timeOfCreation)
//              FROM card
//              WHERE timeOfCreation < (
//                SELECT timeOfCreation
//                FROM card
//                WHERE id = current_id
//              )
//            )
//            """)
//    Optional<String> getPreviousCardId(@Param("current_id") String id);

//    @Query("""
//            SELECT id
//            FROM card
//            WHERE time_of_creation = (
//              SELECT MIN(time_of_creation)
//              FROM card
//              WHERE time_of_creation < (
//                SELECT time_of_creation
//                FROM card
//                WHERE id = current_id
//              )
//            )
//            """)
//@Query("""
//            SELECT id
//            FROM card
//            WHERE timeOfCreation = (
//              SELECT MIN(timeOfCreation)
//              FROM card
//              WHERE timeOfCreation < (
//                SELECT timeOfCreation
//                FROM card
//                WHERE id = current_id
//              )
//            )
//            """)
//    Optional<String> getNextCardId(@Param("current_id") String id);

//    @Query("""
//            SELECT new com.petrenko.flashcards.dto.TestDto(id)
//            FROM Card
//            WHERE timeOfCreation = (
//              SELECT MIN(timeOfCreation)
//              FROM card
//              WHERE timeOfCreation < (
//                SELECT timeOfCreation
//                FROM card
//                WHERE id = current_id
//              )
//            )
//            """)
//    TestDto getNextCardId(@Param("current_id") String id);

//    @Query("""
//            SELECT new com.petrenko.flashcards.dto.TestDto(c.id)
//            FROM Card c
//            WHERE c.timeOfCreation = (
//              SELECT MAX(c2.timeOfCreation)
//              FROM Card c2
//              WHERE c2.timeOfCreation < (
//                SELECT c3.timeOfCreation
//                FROM Card c3
//                WHERE c3.id = :current_id
//              )
//            )
//            """)
//    Optional<TestDto> getNextCardId(@Param("current_id") String id);

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
    Optional<String> getFirstCardId(); // work

    @Query("""
            SELECT id
            FROM Card
            WHERE timeOfCreation = (
            SELECT MAX(timeOfCreation)
            From Card
            )
                 """)
    Optional<String> getLastCardId(); // work

//    @Query("""
//            SELECT new com.petrenko.flashcards.dto.TestDto(c.id)
//        FROM Card c
//        WHERE c.timeOfCreation = (
//          SELECT MAX(c2.timeOfCreation)
//          FROM Card c2
//          WHERE c2.timeOfCreation < (
//            SELECT CASE
//              WHEN COUNT(c3) = 0 THEN
//                MIN(c4.timeOfCreation)
//              ELSE
//                MAX(c3.timeOfCreation)
//              END
//            FROM Card c3
//            RIGHT JOIN Card c4 ON 1 = 1
//            WHERE c3.id = current_id
//          )
//        )
//            """)
//    TestDto getNextCardId(@Param("current_id") String id);


//    CardViewByIdDto getCardViewByIdDto(String id); // Query
}
