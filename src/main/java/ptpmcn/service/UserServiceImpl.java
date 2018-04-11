package ptpmcn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ptpmcn.dto.UserDto;
import ptpmcn.dto.UserRegistrationDto;
import ptpmcn.errorhandling.ForbiddenException;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Role;
import ptpmcn.model.User;
import ptpmcn.repository.RoleRepository;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findOneByUsername(username);
		AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();
		user.ifPresent(checker::check);
		return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public UserDto save(UserRegistrationDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		user.addRole(roleRepository.findOneByName("MEMBER").get());
		User saveUser = userRepository.save(user);
		return modelMapper.map(saveUser, UserDto.class);
	}

	@Override
	public Optional<UserDto> findOne(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.map(u -> modelMapper.map(u, UserDto.class));

	}

	@Override
	public Page<UserDto> findPaginated(int page, int size, Direction direction, String feild) {
		if (feild.equals("vote")) {
			return userRepository
					.findAndSortByVote(PageRequest.of(page, size, JpaSort.unsafe(direction, "sum(a.vote)")))
					.map(u -> modelMapper.map(u, UserDto.class));

		}

		if (feild.equals("follow")) {
			return userRepository
					.findAndSortByFollower(PageRequest.of(page, size, JpaSort.unsafe(direction, "followingUsers.size")))
					.map(u -> modelMapper.map(u, UserDto.class));

		}
		return userRepository.findAll(PageRequest.of(page, size, direction, feild))
				.map(u -> modelMapper.map(u, UserDto.class));
	}

	@Override
	public void banUser(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		User user = userOptional.orElseThrow(ResourceNotFoundException::new);
		Role memberRole = roleRepository.findOneByName("MEMBER").get();
		if (user.isAdmin() || !user.getRoles().contains(memberRole)) {
			throw new ForbiddenException();
		}
		user.removeRole(memberRole);
	}

	@Override
	public void unbanUser(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		User user = userOptional.orElseThrow(ResourceNotFoundException::new);
		if (user.getRoles().isEmpty()) {
			user.addRole(roleRepository.findOneByName("MEMBER").get());
		} else {
			throw new ForbiddenException();
		}
	}
}
