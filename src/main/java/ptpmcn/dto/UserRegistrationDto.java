package ptpmcn.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptpmcn.validator.FieldsValueMatch;
import ptpmcn.validator.UniqueLogin;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch(field = "password", fieldMatch = "repassword", message = "Password does not match.")
public class UserRegistrationDto {
	@NotBlank(message= "{NotBlank.userRegistration.username}")
	@Size(min = 6, message= "{Size.userRegistration.username}")
	@UniqueLogin
	private String username;
	
	@NotBlank(message= "{NotBlank.userRegistration.password}")
	@Size(min = 6, message= "{Size.userRegistration.password}")
	private String password;
	
	@NotBlank(message= "{NotBlank.userRegistration.repassword}")
	private String repassword;
	
	@NotBlank(message= "{NotBlank.userRegistration.fullname}")
	private String fullname;
	
	@NotBlank(message= "{NotBlank.userRegistration.email}")
	@Email(message= "{Email.userRegistration.email}")
	private String email;

}
