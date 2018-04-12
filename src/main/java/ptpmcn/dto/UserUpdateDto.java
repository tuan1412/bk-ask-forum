package ptpmcn.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
	@NotBlank
	private String fullname;
	@NotBlank
	private String email;
}
