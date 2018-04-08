package ptpmcn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ptpmcn.dto.QuestionDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Question;
import ptpmcn.model.User;
import ptpmcn.repository.QuestionRepository;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class QuestionFollowServiceImpl implements QuestionFollowService {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Page<QuestionDto> findPaginatedFollow(int page, int size, Direction direction, String feild) {
		Optional<User> user = securityContextService.getCurrentUser();
		Long id = user.orElseThrow(ResourceNotFoundException::new).getId();
		return questionRepository.findFollowQuestionByUserId(id, PageRequest.of(page, size, direction, feild))
				.map(q -> modelMapper.map(q, QuestionDto.class));
	}

	@Override
	public void followQuestion(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Optional<User> currentUserWithFetch = userRepository.findById(currentUser.get().getId());
		Optional<Question> question = questionRepository.findById(id);
		question.ifPresent(q -> currentUserWithFetch.get().followQuestion(q));
		userRepository.save(currentUserWithFetch.get());
		question.orElseThrow(ResourceNotFoundException::new);	
	}

	@Override
	public void unfollowQuestion(Long id) {
		Optional<User> currentUser = securityContextService.getCurrentUser();
		Optional<User> currentUserWithFetch = userRepository.findById(currentUser.get().getId());

		Optional<Question> question = questionRepository.findById(id);
		question.ifPresent(q -> currentUserWithFetch.get().unfollowQuestion(q));
		userRepository.save(currentUserWithFetch.get());
		question.orElseThrow(ResourceNotFoundException::new);	
		
	}
}
