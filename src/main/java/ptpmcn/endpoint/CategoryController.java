package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Category;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.CategoryService;
import ptpmcn.util.SortUtil;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public List<Category> getPageCategory(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<Category> resultPage = categoryService.findPaginated(page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Category>(Category.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public Category saveCategory(@Valid @RequestBody Category category) {
		return categoryService.save(category);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("{id}")
	public void deleteCategory(@PathVariable("id") Long id) {
		categoryService.delete(id);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("{id}")
	public void updateCategory(@PathVariable("id") Long id, @Valid @RequestBody Category category) {
		if (!categoryService.update(id, category)) {
			throw new ResourceNotFoundException();
		}
	}

}
