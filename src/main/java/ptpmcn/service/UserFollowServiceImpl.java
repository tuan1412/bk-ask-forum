package ptpmcn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ptpmcn.dto.UserDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.User;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class UserFollowServiceImpl implements UserFollowService {
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Page<UserDto> findPaginatedFollow(int page, int size, Direction direction, String feild) {
		Optional<User> user = securityContextService.getCurrentUser();
		Long id = user.orElseThrow(ResourceNotFoundException::new).getId();
		return userRepository.findFollowUser(id, PageRequest.of(page, size, direction, feild))
				.map(q -> modelMapper.map(q, UserDto.class));
	}

	@Override
	public void followUser(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Optional<User> currentUserWithFetch = userRepository.findById(currentUser.get().getId());
		Optional<User> followUser = userRepository.findById(id);
		followUser.ifPresent(u -> currentUserWithFetch.get().followUser(u));
		userRepository.save(currentUserWithFetch.get());
		followUser.orElseThrow(ResourceNotFoundException::new);	

	}

	@Override
	public void unfollowUser(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Optional<User> currentUserWithFetch = userRepository.findById(currentUser.get().getId());
		Optional<User> followUser = userRepository.findById(id);
		followUser.ifPresent(u -> currentUserWithFetch.get().unfollowUser(u));
		userRepository.save(currentUserWithFetch.get());
		followUser.orElseThrow(ResourceNotFoundException::new);	

	}

	@Override
	public boolean isFollowed(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Long uid = currentUser.get().getId();
		Optional<User> user = userRepository.findFollowed(id, uid);
		return user.isPresent();
	}

}
