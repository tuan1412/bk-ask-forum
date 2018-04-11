package ptpmcn.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ptpmcn.repository.CategoryRepository;

public class CategoryValidator implements ConstraintValidator<ExistedCategory, String>{
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		return name != null || !categoryRepository.findOneByName(name).isPresent() ;
	}

}
