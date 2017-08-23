package worm.esort.repository;

import org.springframework.data.repository.CrudRepository;

import worm.esort.domain.Tagtype;


public interface TagTypeRepository extends CrudRepository<Tagtype, Long>{

	Tagtype findTagTypeByTypeName(String typeName);
}
