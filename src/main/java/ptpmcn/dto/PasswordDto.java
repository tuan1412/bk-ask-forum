package ptpmcn.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptpmcn.validator.FieldsValueMatch;
import ptpmcn.validator.OldPassword;

@Getter
@Setter
@NoArgsConstructor
@FieldsValueMatch(field = "newPassword", fieldMatch = "renewPassword", message = "Password does not match.")
public class PasswordDto {
	@NotBlank	@OldPassword
	private String oldPassword;
	@NotBlank
	private String newPassword;
	@NotBlank
	private String renewPassword;
	
	
}
