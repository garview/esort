package worm.esort.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import worm.esort.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	UserRepository userResp;
	
	@Test
	public void testFlow() {
		User u = new User();
		u.setUsername("admin");
		u.setPassword("admin");
		userResp.save(u);
	}

}
