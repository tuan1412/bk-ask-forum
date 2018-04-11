package ptpmcn.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptpmcn.validator.ExistedCategory;

@Getter
@Setter
@NoArgsConstructor
public class QuestionCreateDto {
	
	@NotBlank(message="{NotBlank.questionCreateDto.title}")
	private String title;
	
	@NotBlank(message="{NotBlank.questionCreateDto.content}")
	private String content;
	
	@ExistedCategory
	private String category;

}
