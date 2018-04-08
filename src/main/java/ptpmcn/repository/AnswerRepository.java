package ptpmcn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptpmcn.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	
	@Query("select a from Answer a where a.question.id = :id")
	Page<Answer> findByQuestion(@Param("id") Long id, Pageable pageable);
	
	@Query("select a from Answer a where a.user.id = :id")
	Page<Answer> findByUserId(@Param("id") Long id, Pageable pageable);

	

}
