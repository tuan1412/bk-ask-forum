package ptpmcn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ptpmcn.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Optional<Category> findOneByName(String category);

}
