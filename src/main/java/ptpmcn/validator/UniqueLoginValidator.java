package ptpmcn.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ptpmcn.repository.UserRepository;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String>{

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return username != null && !userRepository.findOneByUsername(username).isPresent();
	}

}
