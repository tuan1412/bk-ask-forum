package ptpmcn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ptpmcn.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByUsername(String username);

}
