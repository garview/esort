package worm.esort.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import worm.esort.domain.Book;
import worm.esort.domain.Score;
import worm.esort.domain.User;
import worm.esort.dto.BookScore;
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
	
	public Iterable<BookScore> getBookScores(){
		List<Object[]> result = bookResp.findBookScores(1L);
		List<BookScore> newResult = new ArrayList<BookScore>();
		result.stream().forEach(obj->{
			for(int i=0; i<obj.length; i++){
				System.out.print("--"+obj[i]);
			}
			System.out.println();
			BookScore bs = new BookScore();
			bs.setName((String) obj[0]);
			bs.setLength((Integer)obj[1]);
			bs.setRatingCount((Integer)obj[2]);
			bs.setFavourited((Integer)obj[3]);
			bs.setAverageRating((Float) obj[4]);
			bs.seteInputDate((Date)obj[5]);
			bs.setHref((String)obj[6]);
			if(obj[7]!=null)
				bs.setScore((Float) obj[7]);
			bs.setBookId(Long.valueOf(obj[8].toString()));
			newResult.add(bs);
		});
		return newResult;
	}
}
