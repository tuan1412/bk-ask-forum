package ptpmcn.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private String content;
	private String categoryName;
	private long vote;
	private long userId;
	private String userUsername;
	private String userAvatar;
	private List<Long> voteUserIds;
}
