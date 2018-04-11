package ptpmcn.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ptpmcn.model.Category;
import ptpmcn.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Page<Category> findPaginated(int page, int size, Direction direction, String feild) {
		return categoryRepository.findAll(PageRequest.of(page, size, direction, feild));
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void delete(Long id) {
		categoryRepository.deleteById(id);

	}

	@Override
	public boolean update(Long id, Category category) {
		Optional<Category> existCategory = categoryRepository.findById(id);
		if (existCategory.isPresent()) {
			existCategory.get().setName(category.getName());
			return true;
		}
		return false;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

}
