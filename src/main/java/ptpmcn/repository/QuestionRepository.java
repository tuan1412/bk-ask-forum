package ptpmcn.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptpmcn.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	@Query("select q from Question q where q.user.id = :id")
	Page<Question> findByUserId(@Param("id") Long id, Pageable pageable);
	
	@Query("select q, count(u.id) as vote, q.lastModified as time "
			+ "from Question q left join q.voteUsers u where q.user.id = :id group by q.id order by vote desc,time desc ")
	Page<Object[]> findByUserAndSortByVote(@Param("id") Long id, Pageable pageable);
	
	@Query("select q from Question q where q.category.name = :name")
	Page<Question> findByCategory(@Param("name") String name, Pageable pageable);
	
	@Query("select q, count(u.id) as vote, q.lastModified as time "
			+ "from Question q left join q.voteUsers u where q.category.name = :name group by q.id order by vote desc,time desc ")
	Page<Object[]> findByCategoryAndSortByVote(@Param("name") String name, Pageable pageable);
	
	@Query("select q from Question q where q.answers.size = :size")
	Page<Question> findByAnswer(@Param("size") int size, Pageable pageable);
	
	@Query("select q, count(u.id) as vote, q.lastModified as time "
			+ "from Question q left join q.voteUsers u where q.answers.size = :size group by q.id order by vote desc,time desc ")
	Page<Object[]> findByAnswerAndSortByVote(@Param("size") int size, Pageable pageable);
	
	@Query("select q from Question q left join q.users u where u.id = :id")
	Page<Question> findFollowQuestionByUserId(@Param("id") Long id, Pageable pageable);

	@Query("select q from Question q where q.title like %:keyword% or q.content like %:keyword% or q.user.username like %:keyword%")
	Page<Question> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select q, count(u.id) as vote, q.lastModified as time "
			+ "from Question q left join q.voteUsers u "
			+ "where q.title like %:keyword% or q.content like %:keyword% or q.user.username like %:keyword%"
			+ " group by q.id order by vote desc,time desc ")
	Page<Object[]> findByKeywordAndSortByVote(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select count(q.id) from Question q join q.voteUsers where q.id =:id")
	Long countVote(@Param("id") Long id);
	
	@Query("select q from Question q left join q.voteUsers u where q.id =:qid and u.id =:uid")
	Optional<Question> findVoted(@Param("qid")Long qid, @Param("uid")Long uid);

	@Query("select q from Question q left join q.users u where q.id =:qid and u.id =:uid")
	Optional<Question> findFollowed(@Param("qid")Long qid, @Param("uid")Long uid);
}
