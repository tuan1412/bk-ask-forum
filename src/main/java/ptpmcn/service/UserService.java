package ptpmcn.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.UserDetailsService;

import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {
	UserDto save(UserRegistrationDto userDto);

	Optional<UserDto> findOne(Long id);

	Page<UserDto> findPaginated(int page, int size, Direction direction, String feild);

	void banUser(Long id);

	void unbanUser(Long id);
	

}
