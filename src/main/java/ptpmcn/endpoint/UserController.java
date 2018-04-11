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
import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.User;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto register(@Valid @RequestBody UserRegistrationDto userDto) {
		return userService.save(userDto);
	}

	@PostMapping("/paginated")
	public List<UserDto> getPageUser(@RequestBody PaginatedParam params, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		
		Page<UserDto> resultPage = userService.findPaginated(params.getPage(), 
															params.getSize(), 
															params.getDirection(),
															params.getFeild());
		if (params.getPage() > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<User>(User.class, uriBuilder, response,
				params.getPage(), resultPage.getTotalPages(), params.getSize()));
		return resultPage.getContent();

	}

	@PostMapping("/{id}")
	public UserDto getOne(@PathVariable("id") Long id) {
		return userService.findOne(id).orElseThrow(ResourceNotFoundException::new);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("{id}/ban")
	public void banUser(@PathVariable("id") Long id) {
		userService.banUser(id);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("{id}/unban")
	public void unbanUser(@PathVariable("id") Long id) {
		userService.unbanUser(id);
	}

}
