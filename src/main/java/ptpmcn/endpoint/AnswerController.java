package ptpmcn.endpoint;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.AnswerCreateDto;
import ptpmcn.dto.AnswerDto;
import ptpmcn.dto.PaginatedParam;
import ptpmcn.dto.SuccessDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Answer;
import ptpmcn.model.User;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.AnswerService;
import ptpmcn.service.NotificationService;
import ptpmcn.service.SecurityContextService;
import ptpmcn.service.UserService;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private NotificationService notificationService;
	
	@PostMapping("user/{id}/paginated")
	public List<AnswerDto> getPageAnswers(@RequestBody PaginatedParam params, @PathVariable("id") Long id,
										UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		
		int page = params.getPage();
		int size = params.getSize();
		Direction direction = params.getDirection();
		String feild = params.getFeild();
		
		Page<AnswerDto> resultPage = answerService.findPaginatedByUserId(id, page, size, direction, feild);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Answer>(Answer.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();

	}
	
	@PostMapping("question/{id}/paginated")
	public List<AnswerDto> getPageAnswerDtoByQuestion(@RequestBody PaginatedParam params, @PathVariable("id") Long id,
													UriComponentsBuilder uriBuilder, HttpServletResponse response){

		int page = params.getPage();
		int size = params.getSize();
				
		Page<AnswerDto> resultPage = answerService.findPaginatedByQuestionId(id, page, size);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Answer>(Answer.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("question/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public AnswerDto createAnswer(@Valid@RequestBody AnswerCreateDto answerDto, @PathVariable("id") Long id){
		AnswerDto dto =  answerService.createAnswer(id, answerDto);
		
		Optional<User> opUser =  securityContextService.getCurrentUser();
		Long uid = opUser.get().getId();
		List<Long> userIds = userService.findNotifyUser(uid, id);
		userIds.stream().forEach(x -> template.convertAndSend("/notify/" + x, notificationService.createNotification(x, id)));
		return dto;
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/update")
	public AnswerDto updateAnswer(@PathVariable("id") Long id, @Valid@RequestBody AnswerCreateDto answerDto) {
		return answerService.updateAnswer(id, answerDto);
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/delete")
	public SuccessDto deleteAnswer(@PathVariable("id") Long id) {
		answerService.delete(id);
		return new SuccessDto();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/vote")
	public AnswerDto voteAnswer(@PathVariable("id") Long id) {
		return answerService.upVote(id);
	}	
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/unvote")
	public AnswerDto unvoteAnswer(@PathVariable("id") Long id) {
		return answerService.downVote(id);
	}	
	
	@PostMapping("{id}")
	public AnswerDto getOne(@PathVariable("id") Long id) {
		return answerService.findOne(id);
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("{id}/check")
	public SuccessDto checkVote(@PathVariable("id") Long id) {
		if (answerService.isVoted(id)) {
			return new SuccessDto();
		}
		throw new ResourceNotFoundException();
	}
}
