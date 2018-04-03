package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.User;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.repository.UserRepository;
import ptpmcn.service.UserService;
import ptpmcn.util.SortUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User register(@Valid @RequestBody UserRegistrationDto userDto) {
		return userService.save(userDto);
	}
	
	@GetMapping(params= {"username"})
	public User get(@RequestParam("username") String username) {
		return userRepository.findOneByUsername(username).get();
	}
	@GetMapping()
	public List<UserDto> getPageUser(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1") int size,
			@RequestParam(name = "sort", required = false, defaultValue = "+id") String sort,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Page<UserDto> resultPage = userService.findPaginated(page, size, SortUtil.getDirection(sort),
				SortUtil.getFeild(sort));
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<UserDto>(UserDto.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}

	@GetMapping("/{id}")
	public UserDto getOne(@PathVariable("id") Long id) {
		return userService.findOne(id).orElseThrow(ResourceNotFoundException::new);
	}
}
