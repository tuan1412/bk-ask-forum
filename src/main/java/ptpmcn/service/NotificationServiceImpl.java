package ptpmcn.service;


import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ptpmcn.dto.NotificationDto;
import ptpmcn.model.Notification;
import ptpmcn.model.Question;
import ptpmcn.model.User;
import ptpmcn.repository.NotificationRepository;
import ptpmcn.repository.QuestionRepository;
import ptpmcn.repository.UserRepository;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public NotificationDto createNotification(Long uid, Long qid) {
		User user = userRepository.findById(uid).get();
		User currentUser = securityContextService.getCurrentUser().get();
		Question question = questionRepository.findById(qid).get();
		Notification noti = new Notification();
		String content = currentUser.getFullname() + " đã trả lời câu hỏi " + question.getTitle();
		
		noti.setContent(content);
		noti.setQuestion(question);
		noti.setUser(user);
		
		return modelMapper.map(notificationRepository.save(noti), NotificationDto.class);
	}

	@Override
	public NotificationDto createQuestionNotification(Long uid, Long qid) {
		User user = userRepository.findById(uid).get();
		User currentUser = securityContextService.getCurrentUser().get();
		Question question = questionRepository.findById(qid).get();
		Notification noti = new Notification();
		String content = currentUser.getUsername() + " đã tạo câu hỏi " + question.getTitle();
		
		noti.setContent(content);
		noti.setQuestion(question);
		noti.setUser(user);
		
		return modelMapper.map(notificationRepository.save(noti), NotificationDto.class);
	}

	@Override
	public Page<NotificationDto> findPaginated(int page, int size) {
		User currentUser = securityContextService.getCurrentUser().get();
		Long uid = currentUser.getId();
		return notificationRepository.findAllByUserId(uid, PageRequest.of(page, size))
				.map(x -> modelMapper.map(x, NotificationDto.class));
	}

	@Override
	public long countNotSeenNotification() {
		User currentUser = securityContextService.getCurrentUser().get();
		Long uid = currentUser.getId();
		return notificationRepository.countNotSeenNotification(uid);
	}

	@Override
	public int seenNotification() {
		User currentUser = securityContextService.getCurrentUser().get();
		Long uid = currentUser.getId();
		return notificationRepository.seenNotification(uid);
	}

}
