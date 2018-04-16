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
import ptpmcn.repository.AnswerRepository;
import ptpmcn.repository.QuestionRepository;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Override
	public Page<AnswerDto> findPaginatedByQuestionId(Long id, int page, int size) {
		return answerRepository.findByQuestion(id, PageRequest.of(page, size, Direction.DESC, "vote", "lastModified"))
				.map(a -> modelMapper.map(a, AnswerDto.class));
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
	public void updateAnswer(Long id, AnswerCreateDto answerDto) {
		Optional<Answer> answer = answerRepository.findById(id);
		answer.ifPresent(a -> {
			a.setContent(answerDto.getContent());
			answerRepository.save(a);
		});
		answer.orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public void upVote(Long id) {
		Optional<Answer> answer = answerRepository.findById(id);
		answer.ifPresent(a -> {
			a.setVote(a.getVote() + 1);
			answerRepository.save(a);
		});
		answer.orElseThrow(ResourceNotFoundException::new);
	}

}
