package ptpmcn.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ptpmcn.dto.AnswerDto;
import ptpmcn.repository.AnswerRepository;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AnswerRepository answerRepository;

	@Override
	public Page<AnswerDto> findPaginatedByQuestionId(Long id, int page, int size) {
		return answerRepository.findByQuestion(id, PageRequest.of(page, size, Direction.DESC, "vote", "lastModified"))
				.map(a -> modelMapper.map(a, AnswerDto.class));
	}

}
