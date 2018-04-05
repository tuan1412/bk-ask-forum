package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.AnswerDto;
import ptpmcn.dto.QuestionDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.AnswerService;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private AnswerService answerService;
	
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
}
