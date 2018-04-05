package ptpmcn.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerCreateDto {
	
	@NotBlank(message = "{NotBlank.AnswerCreateDto.content")
	private String content;
	
}
