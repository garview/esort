package worm.esort.service;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import worm.esort.domain.Book;
import worm.esort.domain.Score;
import worm.esort.domain.User;
import worm.esort.repository.BookRepository;
import worm.esort.repository.ScoreRepository;
import worm.esort.repository.UserRepository;

@Service
@Transactional
public class ScoreService {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	BookRepository bookResp;
	@Autowired
	UserRepository userResp;
	@Autowired
	ScoreRepository scoreResp;

	public void giveMark(long userId, long bookId,float score) {
		Book b = bookResp.findOne(bookId);
		User u = userResp.findOne(userId);
		Score s = scoreResp.findScoreByBookAndUser(b, u);
		if(s==null)
			s = new Score();
		s.setBook(b);
		s.setUser(u);
		s.setScore(score);
		scoreResp.save(s);
	}
}
