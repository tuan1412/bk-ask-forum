package ptpmcn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import ptpmcn.dto.QuestionDto;

public interface QuestionFollowService {

	Page<QuestionDto> findPaginatedFollow(int page, int size, Direction direction, String feild);

	void followQuestion(Long id);

	void unfollowQuestion(Long id);

}
