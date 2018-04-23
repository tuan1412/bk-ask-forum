package ptpmcn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import ptpmcn.dto.AnswerCreateDto;
import ptpmcn.dto.AnswerDto;

public interface AnswerService {

	Page<AnswerDto> findPaginatedByQuestionId(Long id, int page, int size);

	Page<AnswerDto> findPaginatedByUserId(Long id, int page, int size, Direction direction, String feild);

	AnswerDto createAnswer(Long id, AnswerCreateDto answerDto);

	void delete(Long id);

	AnswerDto updateAnswer(Long id, AnswerCreateDto answerDto);

	AnswerDto upVote(Long id);

	AnswerDto findOne(Long id);

	AnswerDto downVote(Long id);
}
