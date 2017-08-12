package worm.esort.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import worm.esort.domain.Tag;


public interface TagRepository extends CrudRepository<Tag, Long>{

	@Lock(LockModeType.WRITE)
	Tag findTagByTagName(String tagName);
}
