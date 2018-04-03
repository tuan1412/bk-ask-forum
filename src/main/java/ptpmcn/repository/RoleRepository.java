package ptpmcn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ptpmcn.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findOneByName(String name);
}
