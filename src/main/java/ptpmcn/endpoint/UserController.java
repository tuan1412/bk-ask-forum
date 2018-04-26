package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.PaginatedParam;
import ptpmcn.dto.PasswordDto;
import ptpmcn.dto.SuccessDto;
import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.dto.UserUpdateDto;
import ptpmcn.errorhandling.FileUploadException;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.User;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.SecurityContextService;
import ptpmcn.service.StorageService;
import ptpmcn.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private StorageService storageService;
	
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
	public SuccessDto banUser(@PathVariable("id") Long id) {
		userService.banUser(id);
		return new SuccessDto();
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("{id}/unban")
	public SuccessDto unbanUser(@PathVariable("id") Long id) {
		userService.unbanUser(id);
		return new SuccessDto();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/changeProfile")
	public UserDto changeProfile(@RequestPart("user") @Valid UserUpdateDto userDto, 
								@RequestPart("file") MultipartFile file) {
		
		User user = securityContextService.getCurrentUser().orElseThrow(ResourceNotFoundException::new);
		user.setFullname(userDto.getFullname());
		user.setEmail(userDto.getEmail());
		if (file != null) {
			try {
				storageService.store(file);
				user.setAvatar(file.getOriginalFilename());
			}catch (FileUploadException e) {
				e.printStackTrace();
			} 
		}
		return userService.update(user);	
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/changePass")
	public UserDto changePass(@Valid@RequestBody PasswordDto passwordDto) {
		User user = securityContextService.getCurrentUser().orElseThrow(ResourceNotFoundException::new);
		String passwordEncode = passwordEncoder.encode(passwordDto.getNewPassword());
		user.setPassword(passwordEncode);
		return userService.update(user);
	}
}
