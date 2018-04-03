package ptpmcn.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SortUtil {	
	public static Direction getDirection(String sort) {
		if (sort.startsWith("-")) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}
	
	public static String getFeild(String sort) {
		return sort.substring(1);
	}
}
