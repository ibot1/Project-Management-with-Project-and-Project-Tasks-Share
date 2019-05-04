package ibot.komo.tobi.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ibot.komo.tobi.models.Users;



@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Users.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		
		Users user = (Users)target;
		System.out.println(user.getConfirmPasswd() + " " + user.getPassword());
			
		if(user.getPassword().length() < 6) {
			errors.rejectValue("password", "Length", "Password must be at least 6 characters");
		}
		
		if(!user.getPassword().equals(user.getConfirmPasswd())) {
			System.out.println(user.getPassword().equals(user.getConfirmPasswd()) + " ");
			errors.rejectValue("confirmPasswd","Match","Passwords must match");
		}
	}
}
