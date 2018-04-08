package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.QuestionDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.QuestionFollowService;
import ptpmcn.util.SortUtil;

@RestController
public class QuestionFollowController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private QuestionFollowService questionFollowService;

	@GetMapping("/api/questions/follow")
	public List<QuestionDto> getPageQuestionFollow(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<QuestionDto> resultPage = questionFollowService.findPaginatedFollow(page, size,
				SortUtil.getDirection(sort), SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<QuestionDto>(QuestionDto.class, uriBuilder,
				response, page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@GetMapping("/api/questions/{id}/follow")
	public void followQuestion(@PathVariable("id") Long id) {
		questionFollowService.followQuestion(id);
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@GetMapping("/api/questions/{id}/unfollow")
	public void unfollowQuestion(@PathVariable("id") Long id) {
		questionFollowService.unfollowQuestion(id);
	}
}
