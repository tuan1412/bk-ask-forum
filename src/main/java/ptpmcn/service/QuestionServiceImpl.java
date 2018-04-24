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
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Category;
import ptpmcn.model.Question;
import ptpmcn.model.User;
import ptpmcn.repository.CategoryRepository;
import ptpmcn.repository.QuestionRepository;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private QuestionRepository questionRepository;
	
	@Override
	public QuestionDto save(QuestionCreateDto questionDto) {
		Category category = categoryRepository.findOneByName(questionDto.getCategory()).get();
		Question question = modelMapper.map(questionDto, Question.class);
		category.addQuestion(question);
		return modelMapper.map(questionRepository.save(question), QuestionDto.class);
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
	public QuestionDto update(Long id, QuestionCreateDto questionDto) {
		Optional<Category> category = categoryRepository.findOneByName(questionDto.getCategory());
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent() && category.isPresent()) {
			question.get().setContent(questionDto.getContent());
			question.get().setTitle(questionDto.getTitle());
			category.get().addQuestion(question.get());
			return modelMapper.map(questionRepository.save(question.get()), QuestionDto.class);
		}else {
			throw new ResourceNotFoundException();
		}
	}

	@Override
	public Question update(Question question) {
		return questionRepository.save(question);
	}

	@Override
	public QuestionDto findOne(Long id) {
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent()) {
			return modelMapper.map(questionRepository.findById(id).get(), QuestionDto.class);
		}else {
			throw new ResourceNotFoundException();
		}
		
	}

	@Override
	public QuestionDto map(Question question) {
		return modelMapper.map(question, QuestionDto.class);
	}

	@Override
	public Page<QuestionDto> findPaginatedByKeyword(String keyword, int page, int size, Direction direction,
			String feild) {
		return questionRepository.findByKeyword(keyword, PageRequest.of(page, size, direction, feild))
				.map(u -> modelMapper.map(u, QuestionDto.class));
	}

	@Override
	public QuestionDto voteQuestion(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		User user = userRepository.findOneById(currentUser.get().getId());
		Optional<Question> question = questionRepository.findById(id);
		question.orElseThrow(ResourceNotFoundException::new);
		if (question.isPresent()) {
			user.voteQuestion(question.get());
		}
		return modelMapper.map(questionRepository.save(question.get()), QuestionDto.class);
	}

	@Override
	public QuestionDto unvoteQuestion(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		User user = userRepository.findOneById(currentUser.get().getId());
		Optional<Question> question = questionRepository.findById(id);
		question.orElseThrow(ResourceNotFoundException::new);
		if (question.isPresent()) {
			user.unvoteQuestion(question.get());
		}
		return modelMapper.map(questionRepository.save(question.get()), QuestionDto.class);
	}

	@Override
	public boolean isVoted(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Long uid = currentUser.get().getId();
		Optional<Question> question = questionRepository.findVoted(id, uid);
		return question.isPresent();
	}
}
