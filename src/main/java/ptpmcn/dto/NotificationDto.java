package ptpmcn.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {
	private Long id;
	private Long userId;
	private Long questionId;
	private String content;
	private LocalDateTime createAt;
	private boolean seen;
	
}
