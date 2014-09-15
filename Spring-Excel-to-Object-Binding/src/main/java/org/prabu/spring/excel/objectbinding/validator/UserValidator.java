package org.prabu.spring.excel.objectbinding.validator;

import org.prabu.spring.excel.objectbinding.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Component
public class UserValidator extends LocalValidatorFactoryBean implements
		Validator {

	@Override
		public boolean supports(Class<?> clazz) {
			// TODO Auto-generated method stub
			return User.class.equals(clazz);
		}
	
 @Override
public void validate(Object target, Errors errors) {
	// TODO Auto-generated method stub
	//super.validate(target, errors);
	 User user = (User) target;
	 
	 // Name is Required value
	 if(!StringUtils.hasText(user.getName())){
			errors.rejectValue("name", "user.name.required","User Name is Required");
		}
	 
}

}
