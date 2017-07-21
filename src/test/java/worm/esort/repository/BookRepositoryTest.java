package worm.esort.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import worm.esort.domain.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookRepositoryTest {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	BookRepository bookResp;

	@Test
	public void testFindByAvailable() {
		Iterable<Book> it = bookResp.findByAvailable(true);
		for(Book b : it)
			logger.info(b);
	}

}
