package worm.esort.service;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import worm.esort.domain.User;
import worm.esort.repository.UserRepository;

@Service
@Transactional
public class UserService {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	UserRepository userResp;

	public void createUser(String username, Integer sleepTime) {
		try {
			User u = userResp.findUserByUsername(username);
			if (u == null) {
				u = new User();
				u.setUsername(username);
				u.setPassword("1");
				if (sleepTime != null) {
					Thread.sleep(sleepTime * 1000);
				}
				userResp.save(u);
			}
		} catch (Exception e) {
			logger.catching(e);
		}

	}

	public void createUser(String username) throws InterruptedException {
		createUser(username, null);
	}

	public void deleteUser(String username) {
		try {
			User u = userResp.findUserByUsername(username);
			if (u != null)
				userResp.delete(u);
		} catch (Exception e) {
			logger.catching(e);
		}
	}
}
