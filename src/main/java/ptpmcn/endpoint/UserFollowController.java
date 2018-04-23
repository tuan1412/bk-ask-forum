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
import ptpmcn.dto.UserDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.User;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.UserFollowService;

@RestController
public class UserFollowController {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private UserFollowService userFollowService;
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/api/users/follow/paginated")
	public List<UserDto> getPageQuestionFollow(@RequestBody PaginatedParam params, 
												UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		
		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();
		
		Page<UserDto> resultPage = userFollowService.findPaginatedFollow(page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<User>(User.class, uriBuilder,
				response, page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/api/users/{id}/follow")
	public String followUser(@PathVariable("id") Long id) {
		userFollowService.followUser(id);
		return "Follow success";

	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/api/users/{id}/unfollow")
	public String unfollowUser(@PathVariable("id") Long id) {
		userFollowService.unfollowUser(id);
		return "UnFollow success";
	}

}
