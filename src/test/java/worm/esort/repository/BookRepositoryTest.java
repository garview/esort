package worm.esort.repository;

import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import worm.esort.domain.Book;
import worm.esort.dto.BookScore;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookRepositoryTest {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	BookRepository bookResp;

//	@Test
	public void testFindByAvailable() {
		Iterable<Book> it = bookResp.findByAvailable(true);
		for(Book b : it)
			logger.info(b);
	}
//	b.name, b.length, b.ratingCount,b.favourited,b.averageRating,b.eInputDate,b.href,s.score
	@Test
	public void testFindBookScore() {
		List<Object[]> result = bookResp.findBookScores(1L);
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
//			System.out.println(bs);
		});
		System.out.println(result.size());
	}

}
