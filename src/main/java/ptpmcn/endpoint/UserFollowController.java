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
import ptpmcn.dto.UserDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.UserFollowService;
import ptpmcn.util.SortUtil;

@RestController
public class UserFollowController {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private UserFollowService userFollowService;
	
	@GetMapping("/api/users/follow")
	public List<UserDto> getPageQuestionFollow(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<UserDto> resultPage = userFollowService.findPaginatedFollow(page, size,
				SortUtil.getDirection(sort), SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<UserDto>(UserDto.class, uriBuilder,
				response, page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@GetMapping("/api/users/{id}/follow")
	public void followUser(@PathVariable("id") Long id) {
		userFollowService.followUser(id);
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@GetMapping("/api/users/{id}/unfollow")
	public void unfollowUser(@PathVariable("id") Long id) {
		userFollowService.unfollowUser(id);
	}

}
