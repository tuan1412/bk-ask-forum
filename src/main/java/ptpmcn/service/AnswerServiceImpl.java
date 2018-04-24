package ptpmcn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ptpmcn.dto.AnswerCreateDto;
import ptpmcn.dto.AnswerDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Answer;
import ptpmcn.model.Question;
import ptpmcn.model.User;
import ptpmcn.repository.AnswerRepository;
import ptpmcn.repository.QuestionRepository;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {
	
	@Autowired
	private SecurityContextService securityContextService;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Override
	public Page<AnswerDto> findPaginatedByQuestionId(Long id, int page, int size) {
		return answerRepository.findByQuestionAndSort(id, PageRequest.of(page, size))
				.map(a -> modelMapper.map(a[0], AnswerDto.class));
	}

	@Override
	public Page<AnswerDto> findPaginatedByUserId(Long id, int page, int size, Direction direction, String feild) {
		return answerRepository.findByUserId(id, PageRequest.of(page, size, direction, feild))
				.map(a -> modelMapper.map(a, AnswerDto.class));
	}

	@Override
	public AnswerDto createAnswer(Long id, AnswerCreateDto answerDto) {
		Optional<Question> question = questionRepository.findById(id);
		Answer answer = modelMapper.map(answerDto, Answer.class);
		question.orElseThrow(ResourceNotFoundException::new);
		question.ifPresent(q -> q.addAnswer(answer));
		return modelMapper.map(answerRepository.save(answer), AnswerDto.class);
	}

	@Override
	public void delete(Long id) {
		answerRepository.deleteById(id);
	}

	@Override
	public AnswerDto updateAnswer(Long id, AnswerCreateDto answerDto) {
		Optional<Answer> answer = answerRepository.findById(id);
		answer.orElseThrow(ResourceNotFoundException::new);
		if (answer.isPresent()) {
			answer.get().setContent(answerDto.getContent());
		}
		return modelMapper.map(answer.get(), AnswerDto.class);		
	}

	@Override
	public AnswerDto upVote(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		User user = userRepository.findOneById(currentUser.get().getId());
		Optional<Answer> answer = answerRepository.findById(id);
		answer.orElseThrow(ResourceNotFoundException::new);
		if (answer.isPresent()) {
			user.voteAnswer(answer.get());
		}
		return modelMapper.map(answerRepository.save(answer.get()), AnswerDto.class);		
	}

	@Override
	public AnswerDto findOne(Long id) {
		return modelMapper.map(answerRepository.findById(id).get(), AnswerDto.class);
	}

	@Override
	public AnswerDto downVote(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		User user = userRepository.findOneById(currentUser.get().getId());
		Optional<Answer> answer = answerRepository.findById(id);
		answer.orElseThrow(ResourceNotFoundException::new);
		if (answer.isPresent()) {
			user.unvoteAnswer(answer.get());
		}
		return modelMapper.map(answer.get(), AnswerDto.class);
	}

	@Override
	public boolean isVoted(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Long uid = currentUser.get().getId();
		Optional<Answer> answer = answerRepository.findVoted(id, uid);
		return answer.isPresent();
	}

}
