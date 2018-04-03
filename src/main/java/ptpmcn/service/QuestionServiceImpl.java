package ptpmcn.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.model.Category;
import ptpmcn.model.Question;
import ptpmcn.repository.CategoryRepository;
import ptpmcn.repository.QuestionRepository;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
	
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private QuestionRepository questionRepository;
	
	@Override
	public Question save(QuestionCreateDto questionDto) {
		Category category = categoryRepository.findOneByName(questionDto.getCategory()).get();
		System.out.println(category.getName());
		Question question = new Question(questionDto.getContent());
		category.addQuestion(question);
		return questionRepository.save(question);
	}

}
