package ptpmcn.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryPaginatedParam extends PaginatedParam{
	private String name;
}
