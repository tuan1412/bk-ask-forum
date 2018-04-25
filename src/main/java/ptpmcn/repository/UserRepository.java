package ptpmcn.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptpmcn.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByUsername(String username);
    
   	@EntityGraph(value = "User.roles", type = EntityGraphType.LOAD)
	User findOneById(Long id);
	
	@Query("select u from User u left join u.answers a group by u.id")
	Page<User> findAndSortByVote(Pageable pageable);
	
	@Query("select u from User u")
	Page<User> findAndSortByFollower(Pageable pagebale);
	
	@Query("select u from User u left join u.followingUsers fu where fu.id = :id")
	Page<User> findFollowUser(@Param("id") Long id, Pageable pageable);
	
	@Query("select (select count(a.id) from Answer a join a.voteUsers where a.user.id =:id) "
			+"+ (select count(q.id) from Question q join q.voteUsers where q.user.id =:id) from User u")
	Long countVote(@Param("id") Long id);

	@Query("select u from User u left join u.followingUsers fu where fu.id =:id and u.id =:uid")
	Optional<User> findFollowed(@Param("id")Long id, @Param("uid")Long uid); 
}
