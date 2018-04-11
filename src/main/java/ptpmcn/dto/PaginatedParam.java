package ptpmcn.dto;

import org.springframework.data.domain.Sort.Direction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptpmcn.util.SortUtil;

@NoArgsConstructor
@Getter
@Setter
public class PaginatedParam {
	private int page = 0;
	private int size = 1;
	private String sort = "+id";
	
	public Direction getDirection() {
		return SortUtil.getDirection(sort);
	}
	
	public String getFeild() {
		return SortUtil.getFeild(sort);
	}
}
