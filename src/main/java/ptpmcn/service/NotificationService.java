package ptpmcn.service;

import org.springframework.data.domain.Page;

import ptpmcn.dto.NotificationDto;

public interface NotificationService {
	NotificationDto createNotification(Long uid, Long qid); 
	
	NotificationDto createQuestionNotification(Long uid, Long qid);

	Page<NotificationDto> findPaginated(int page, int size);
	
	long countNotSeenNotification();
	
	int seenNotification();
}
