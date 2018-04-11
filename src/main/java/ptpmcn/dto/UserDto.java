package ptpmcn.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String avatar;
	private String username;
	private String fullname;
	private LocalDateTime createAt;
	private String email;
	private int vote;
	private int followers;
	private boolean banned;
	
}
