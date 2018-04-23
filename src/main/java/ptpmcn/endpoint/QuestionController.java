package ptpmcn.endpoint;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.AnswerPaginatedParam;
import ptpmcn.dto.CategoryPaginatedParam;
import ptpmcn.dto.PaginatedParam;
import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.dto.QuestionDto;
import ptpmcn.dto.QuestionSearchDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Question;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private QuestionService questionService;

	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public QuestionDto save(@Valid @RequestBody QuestionCreateDto questionDto) {
		return questionService.save(questionDto);
	}

	@PostMapping("user/{id}/paginated")
	public List<QuestionDto> getPageQuestionByUserId(@RequestBody PaginatedParam params, @PathVariable("id") Long id,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {

		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();
		Page<QuestionDto> resultPage = questionService.findPaginatedByUserId(id, page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Question>(Question.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}

	@PostMapping("category/paginated")
	public List<QuestionDto> getPageQuestionByCategory(@RequestBody CategoryPaginatedParam params,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {

		String name = params.getName();
		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();

		Page<QuestionDto> resultPage = questionService.findPaginatedByCategory(name, page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Question>(Question.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}

	@PostMapping("answer/paginated")
	public List<QuestionDto> getPageQuestionByAnswer(@RequestBody AnswerPaginatedParam params,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {

		int quatity = params.getQuatity();
		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();

		Page<QuestionDto> resultPage = questionService.findPaginatedByAnswers(quatity, page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Question>(Question.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}

	@PostMapping("/paginated")
	public List<QuestionDto> getPageQuestion(@RequestBody PaginatedParam params, UriComponentsBuilder uriBuilder,
			HttpServletResponse response) {

		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();

		Page<QuestionDto> resultPage = questionService.findPaginated(page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<QuestionDto>(QuestionDto.class, uriBuilder,
				response, page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}

	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/delete")
	public QuestionDto delete(@PathVariable("id") Long id) {
		Optional<Question> question = questionService.findOneById(id);
		question.ifPresent(q -> {
			questionService.deleteById(id);
		});
		question.orElseThrow(ResourceNotFoundException::new);

		return questionService.map(question.get());
	}

	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/vote")
	public QuestionDto voteQuestion(@PathVariable("id") Long id) {
		Optional<Question> question = questionService.findOneById(id);
		question.ifPresent(q -> {
			q.setVote(q.getVote() + 1);
			questionService.update(q);
		});
		question.orElseThrow(ResourceNotFoundException::new);
		return questionService.map(question.get());

	}

	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/unvote")
	public QuestionDto unvoteQuestion(@PathVariable("id") Long id) {
		Optional<Question> question = questionService.findOneById(id);
		question.ifPresent(q -> {
			q.setVote(q.getVote() - 1);
			questionService.update(q);
		});
		question.orElseThrow(ResourceNotFoundException::new);
		return questionService.map(question.get());

	}

	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/update")
	public QuestionDto updateQuestion(@Valid @RequestBody QuestionCreateDto questionDto, @PathVariable("id") Long id) {
		return questionService.update(id, questionDto);
	}

	@PostMapping("{id}")
	public QuestionDto getOne(@PathVariable("id") Long id) {
		return questionService.findOne(id);
	}

	@PostMapping("/search")
	public List<QuestionDto> findQuestion(@RequestBody QuestionSearchDto params,UriComponentsBuilder uriBuilder, HttpServletResponse response){
		String keyword = params.getKeyword();
		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();

		Page<QuestionDto> resultPage = questionService.findPaginatedByKeyword(keyword, page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Question>(Question.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
		
	}

}
