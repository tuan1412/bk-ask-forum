package ptpmcn.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import ptpmcn.model.User;
import ptpmcn.service.SecurityContextService;

public class OldPasswordValidator implements ConstraintValidator<OldPassword, String> {
	
	@Autowired
	private SecurityContextService securityContextService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean isValid(String oldPassword, ConstraintValidatorContext context) {
		Optional<User> user = securityContextService.getCurrentUser();
	
		return oldPassword != null && passwordEncoder.matches(oldPassword, user.get().getPassword());
	}


}
