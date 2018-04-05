package ptpmcn.service;

import org.springframework.data.domain.Page;

import ptpmcn.dto.AnswerDto;

public interface AnswerService {

	Page<AnswerDto> findPaginatedByQuestionId(Long id, int page, int size);

}
