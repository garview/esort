package worm.esort.repository;

import org.springframework.data.repository.CrudRepository;

import worm.esort.domain.Book;
import worm.esort.domain.Score;
import worm.esort.domain.User;


public interface ScoreRepository extends CrudRepository<Score, Long>{

	Score findScoreByBookAndUser(Book b, User u);
}
