package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.PaginatedParam;
import ptpmcn.dto.QuestionDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Question;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.QuestionFollowService;

@RestController
public class QuestionFollowController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private QuestionFollowService questionFollowService;

	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/api/questions/follow/paginated")
	public List<QuestionDto> getPageQuestionFollow(@RequestBody PaginatedParam params,
													UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		
		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();
		
		Page<QuestionDto> resultPage = questionFollowService.findPaginatedFollow(page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Question>(Question.class, uriBuilder,
					response, page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/api/questions/{id}/follow")
	public String followQuestion(@PathVariable("id") Long id) {
		questionFollowService.followQuestion(id);
		return "Follow success";
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/api/questions/{id}/unfollow")
	public String unfollowQuestion(@PathVariable("id") Long id) {
		questionFollowService.unfollowQuestion(id);
		return "UnFollow success";
	}
}
