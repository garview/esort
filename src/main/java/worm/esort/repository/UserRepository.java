package worm.esort.repository;

import org.springframework.data.repository.CrudRepository;

import worm.esort.domain.User;


public interface UserRepository extends CrudRepository<User, Long>{

	User findUserByUsername(String username);
}
