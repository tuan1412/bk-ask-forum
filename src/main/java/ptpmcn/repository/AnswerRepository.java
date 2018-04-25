package ptpmcn.repository;

import java.util.Optional;

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

	@Query("select count(a.id) from Answer a  join a.voteUsers where a.id =:id")
	Long countVote(@Param("id") Long id);

	@Query("select a from Answer a left join a.voteUsers u where a.id =:aid and u.id =:uid")
	Optional<Answer> findVoted(@Param("aid")Long id,@Param("uid") Long uid);
	
	@Query("select a, count(u.id) as vote, a.lastModified as time "
			+ "from Answer a left join a.voteUsers u where a.question.id = :id group by a.id order by vote desc,time desc ")
	Page<Object[]> findByQuestionAndSort(@Param("id") Long id, Pageable pageable);
	@Query("select a, count(u.id) as vote, a.lastModified as time "
			+ "from Answer a left join a.voteUsers u where a.user.id = :id group by a.id order by vote desc,time desc ")
	Page<Object[]> findByUserAndSort(@Param("id") Long id, Pageable pageable);

	
	

}
