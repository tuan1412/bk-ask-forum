package ptpmcn.endpoint;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.dto.QuestionDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Question;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.QuestionService;
import ptpmcn.util.SortUtil;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private QuestionService questionService;
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Question save(@Valid @RequestBody QuestionCreateDto questionDto) {
		return questionService.save(questionDto);
	}
	
	@GetMapping("users/{id}")
	public List<QuestionDto> getPageQuestionByUserId(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			@PathVariable("id") Long id,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<QuestionDto> resultPage = questionService.findPaginatedByUserId(id, page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<QuestionDto>(QuestionDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}
	
	@GetMapping("category")
	public List<QuestionDto> getPageQuestionByUserId(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			@RequestParam("name") String name,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<QuestionDto> resultPage = questionService.findPaginatedByCategory(name, page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<QuestionDto>(QuestionDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}
	
	@GetMapping("answers")
	public List<QuestionDto> getPageQuestionByAnswer(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			@RequestParam(name = "quatity", required = true, defaultValue = "0") int sizeOfAnswers,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<QuestionDto> resultPage = questionService.findPaginatedByAnswers(sizeOfAnswers, page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<QuestionDto>(QuestionDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@GetMapping
	public List<QuestionDto> getPageQuestion(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<QuestionDto> resultPage = questionService.findPaginated(page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<QuestionDto>(QuestionDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Long id) {
		questionService.deleteById(id);
	}
	
	@GetMapping("{id}/vote")
	public void voteQuestion(@PathVariable("id") Long id) {
		Optional<Question> question = questionService.findOneById(id);
		question.ifPresent(q -> {
			q.setVote(q.getVote() + 1);
			questionService.update(q);
		});
		question.orElseThrow(ResourceNotFoundException::new);
	}
	
	@PutMapping("{id}")
	public void updateQuestion(@Valid @RequestBody QuestionCreateDto questionDto, @PathVariable("id") Long id) {
		if (!questionService.update(id, questionDto)) {
			throw new ResourceNotFoundException();
		}
	}
}
