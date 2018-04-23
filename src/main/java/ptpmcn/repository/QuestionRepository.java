package ptpmcn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptpmcn.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	@Query("select q from Question q where q.user.id = :id")
	Page<Question> findByUserId(@Param("id") Long id, Pageable pageable);
	
	@Query("select q from Question q where q.category.name = :name")
	Page<Question> findByCategory(@Param("name") String name, Pageable pageable);
	
	@Query("select q from Question q where q.answers.size = :size")
	Page<Question> findByAnswer(@Param("size") int size, Pageable pageable);
	
	@Query("select q from Question q left join q.users u where u.id = :id")
	Page<Question> findFollowQuestionByUserId(@Param("id") Long id, Pageable pageable);

	@Query("select q from Question q where q.title like %:keyword% or q.content like %:keyword% or q.user.username like %:keyword%")
	Page<Question> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
}
