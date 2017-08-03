package worm.esort.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import worm.esort.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	public Iterable<Book> findByAvailable(boolean available);
	public Book findBookByName(String name);
	
	
	/**
	 * sql语句写在Book实体中
	 * @param userId
	 * @return
	 */
	@Query(value="select b.name, b.length, b.ratingCount,b.favourited,b.averageRating,b.eInputDate,b.href,s.score,b.id from book b "
			+ "left join score s on b.id=s.book_id "
			+ "left join user u on u.id=s.user_id "
			+ "where (u.id = :userId or u.id is null) and b.available=true ", nativeQuery = true)
	public List<Object[]> findBookScores(@Param("userId") long userId);
}
