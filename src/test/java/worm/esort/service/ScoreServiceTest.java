package worm.esort.service;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ScoreServiceTest {
	
	@Autowired
	ScoreService scoreService;

	@Test
	public void testGiveMark() {
		scoreService.giveMark(1, 1, 3);
	}

}
