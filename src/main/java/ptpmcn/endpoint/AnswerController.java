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

import ptpmcn.dto.AnswerCreateDto;
import ptpmcn.dto.AnswerDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.AnswerService;
import ptpmcn.util.SortUtil;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private AnswerService answerService;
	
	@GetMapping("user/{id}")
	public List<AnswerDto> getPageAnswers(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			@PathVariable("id") Long id,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<AnswerDto> resultPage = answerService.findPaginatedByUserId(id, page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<AnswerDto>(AnswerDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}
	
	@GetMapping("question/{id}")
	public List<AnswerDto> getPageAnswerDtoByQuestion(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
														@RequestParam(name = "size", required = false, defaultValue = "1") int size,
														@PathVariable("id") Long id,
														UriComponentsBuilder uriBuilder, HttpServletResponse response){
		Page<AnswerDto> resultPage = answerService.findPaginatedByQuestionId(id, page, size);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<AnswerDto>(AnswerDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("question/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public AnswerDto createAnswer(@Valid@RequestBody AnswerCreateDto answerDto, @PathVariable("id") Long id){
		return answerService.createAnswer(id, answerDto);

	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PutMapping("{id}")
	public void updateAnswer(@PathVariable("id") Long id, @Valid@RequestBody AnswerCreateDto answerDto) {
		answerService.updateAnswer(id, answerDto);
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@DeleteMapping("{id}")
	public void deleteAnswer(@PathVariable("id") Long id) {
		answerService.delete(id);
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@GetMapping("{id}/vote")
	public void voteAnswer(@PathVariable("id") Long id) {
		answerService.upVote(id);
	}	
}
