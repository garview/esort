package worm.esort.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest 
public class TagRepositoryTest {
	
	@Autowired
	TagRepository tagResp;
	
//	@Test
	public void testFlow() {
		Assert.assertTrue(tagResp.count()==0);
	}


}
