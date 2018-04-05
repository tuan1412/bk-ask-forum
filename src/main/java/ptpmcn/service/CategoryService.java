package ptpmcn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import ptpmcn.model.Category;

public interface CategoryService {

	Page<Category> findPaginated(int page, int size, Direction direction, String feild);

	Category save(Category category);

	void delete(Long id);

	boolean update(Long id, Category category);

}
