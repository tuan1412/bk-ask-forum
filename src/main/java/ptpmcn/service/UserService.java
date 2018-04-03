package ptpmcn.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.UserDetailsService;

import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.model.User;

public interface UserService extends UserDetailsService {
	User save(UserRegistrationDto userDto);

	Optional<UserDto> findOne(Long id);

	Page<UserDto> findPaginated(int page, int size, Direction direction, String feild);
	

}
