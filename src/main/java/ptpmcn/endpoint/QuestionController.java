package ptpmcn.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ptpmcn.dto.QuestionCreateDto;
import ptpmcn.model.Question;
import ptpmcn.repository.QuestionRepository;
import ptpmcn.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Question save(@Valid @RequestBody QuestionCreateDto questionDto) {
		return questionService.save(questionDto);
	}
	
	@GetMapping
	public List<Question> getAll(){
		return questionRepository.findAll();
	}
}
