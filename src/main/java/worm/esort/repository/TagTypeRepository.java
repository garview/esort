package worm.esort.repository;

import org.springframework.data.repository.CrudRepository;

import worm.esort.domain.TagType;


public interface TagTypeRepository extends CrudRepository<TagType, Long>{

	TagType findTagTypeByTypeName(String typeName);
}
