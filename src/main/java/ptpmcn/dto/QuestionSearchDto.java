package ptpmcn.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSearchDto extends PaginatedParam{
	private String keyword;
}
