package ptpmcn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import ptpmcn.dto.UserDto;


public interface UserFollowService {

	Page<UserDto> findPaginatedFollow(int page, int size, Direction direction, String feild);

	void followUser(Long id);

	void unfollowUser(Long id);

}
