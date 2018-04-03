package ptpmcn.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

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
	private Set<? extends GrantedAuthority> authorities;
	private int vote;
	private int followers;
}
