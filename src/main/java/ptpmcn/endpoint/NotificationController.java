package ptpmcn.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ptpmcn.dto.CountNotiNotSeenDto;
import ptpmcn.dto.NotificationDto;
import ptpmcn.dto.PaginatedParam;
import ptpmcn.dto.SuccessDto;
import ptpmcn.errorhandling.ResourceNotFoundException;
import ptpmcn.model.Notification;
import ptpmcn.pagination.PaginatedResultsRetrievedEvent;
import ptpmcn.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private NotificationService notificationService;
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping
	public List<NotificationDto> getListNotiAndCountNoSeen(@RequestBody PaginatedParam params,
										UriComponentsBuilder uriBuilder, HttpServletResponse response) {
											
		int page = params.getPage();
		int size = params.getSize();
		Page<NotificationDto> resultPage = notificationService.findPaginated(page, size); 
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Notification>(Notification.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));
		return resultPage.getContent();
	}
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/count")
	public CountNotiNotSeenDto count() {
		long number = notificationService.countNotSeenNotification();
		CountNotiNotSeenDto count = new CountNotiNotSeenDto();
		count.setNumber(number);
		return count;
	}
	
	
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MEMBER'})")
	@PostMapping("/seen")
	public SuccessDto seen() {
		notificationService.seenNotification();
		return new SuccessDto();
	}
}
