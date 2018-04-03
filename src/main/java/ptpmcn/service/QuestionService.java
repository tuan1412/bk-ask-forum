package ptpmcn.service;

import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.model.Question;

public interface QuestionService {

	Question save(QuestionCreateDto questionDto);

}
