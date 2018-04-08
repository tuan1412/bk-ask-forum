package ptpmcn.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.dto.QuestionDto;
import ptpmcn.model.Question;

public interface QuestionService {

	QuestionDto save(QuestionCreateDto questionDto);
	
	void deleteById(Long id);
	
	Optional<Question> findOneById(Long id);
	

	Page<QuestionDto> findPaginated(int page, int size, Direction direction, String feild);

	Page<QuestionDto> findPaginatedByUserId(Long id, int page, int size, Direction direction, String feild);

	Page<QuestionDto> findPaginatedByCategory(String name, int page, int size, Direction direction, String feild);

	Page<QuestionDto> findPaginatedByAnswers(int sizeOfAnswers, int page, int size, Direction direction, String feild);

	void update(Long id, QuestionCreateDto questionDto);
	
	Question update(Question question);

}
