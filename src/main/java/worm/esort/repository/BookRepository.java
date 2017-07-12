package worm.esort.repository;

import org.springframework.data.repository.CrudRepository;

import worm.esort.domain.Book;


public interface BookRepository extends CrudRepository<Book, Long>{

}
