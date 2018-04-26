package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.PaginatedParam;
import ptpmcn.dto.SuccessDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Category;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/paginated")
	public List<Category> getPageCategory(@RequestBody PaginatedParam params, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		
		Page<Category> resultPage = categoryService.findPaginated(params.getPage(), 
																params.getSize(), 
																params.getDirection(),
																params.getFeild());
		if (params.getPage() > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Category>(Category.class, uriBuilder, response,
				params.getPage(), resultPage.getTotalPages(), params.getSize()));
		return resultPage.getContent();

	}
	
	@PostMapping("/get")
	public List<Category> getAllCategory(){
		return categoryService.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public Category saveCategory(@Valid @RequestBody Category category) {
		return categoryService.save(category);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("{id}/delete")
	public SuccessDto  deleteCategry(@PathVariable("id") Long id) {
		categoryService.delete(id);
		return new SuccessDto();
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("{id}/update")
	public SuccessDto updateCategory(@PathVariable("id") Long id, @Valid @RequestBody Category category) {
		if (!categoryService.update(id, category)) {
			throw new ResourceNotFoundException();
		}
		return new SuccessDto();
	}
}
