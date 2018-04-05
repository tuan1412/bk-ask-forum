package ptpmcn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.dto.QuestionDto;
import ptpmcn.model.Category;
import ptpmcn.model.Question;
import ptpmcn.repository.CategoryRepository;
import ptpmcn.repository.QuestionRepository;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private ModelMapper modelMapper;
	
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

	@Override
	public Page<QuestionDto> findPaginated(int page, int size, Direction direction, String feild) {
		return questionRepository.findAll(PageRequest.of(page, size, direction, feild))
				.map(u -> modelMapper.map(u, QuestionDto.class));
	}

	@Override
	public Page<QuestionDto> findPaginatedByUserId(Long id, int page, int size, Direction direction, String feild) {
		return questionRepository.findByUserId(id, PageRequest.of(page, size, direction, feild))
				.map(u -> modelMapper.map(u, QuestionDto.class));
	}

	@Override
	public Page<QuestionDto> findPaginatedByCategory(String name, int page, int size, Direction direction,
			String feild) {
		return questionRepository.findByCategory(name, PageRequest.of(page, size, direction, feild))
				.map(u -> modelMapper.map(u, QuestionDto.class));
	}

	@Override
	public Page<QuestionDto> findPaginatedByAnswers(int sizeOfAnswers, int page, int size, Direction direction,
			String feild) {
		return questionRepository.findByAnswer(sizeOfAnswers, PageRequest.of(page, size, direction, feild))
				.map(u -> modelMapper.map(u, QuestionDto.class));
	}

	@Override
	public void deleteById(Long id) {
		questionRepository.deleteById(id);
	}

	@Override
	public Optional<Question> findOneById(Long id) {
		return questionRepository.findById(id);
	}
	
	@Override
	public boolean update(Long id, QuestionCreateDto questionDto) {
		Optional<Category> category = categoryRepository.findOneByName(questionDto.getCategory());
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent() && category.isPresent()) {
			question.get().setContent(questionDto.getContent());
			category.get().addQuestion(question.get());
			questionRepository.save(question.get());
			return true;
		}
		return false;
		
		
	}

	@Override
	public Question update(Question question) {
		return questionRepository.save(question);
	}
}
