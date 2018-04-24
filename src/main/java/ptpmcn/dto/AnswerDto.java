package ptpmcn.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String content;
	private LocalDateTime lastModified;
	private long userId;
	private long questionId;
	private long vote;
}
