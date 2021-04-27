package com.theismann.eventsBeltReview.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.theismann.eventsBeltReview.models.User;
import com.theismann.eventsBeltReview.services.AppService;

@Component
public class UserValidator implements Validator {
	private final AppService appService;
	
	public UserValidator(AppService appService) {
		this.appService = appService;
	}
	 @Override
	    public boolean supports(Class<?> clazz) {
	        return User.class.equals(clazz);
	    }
	    
	    // 2
	    @Override
	    public void validate(Object target, Errors errors) {
	        User user = (User) target;
	        
	        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
	            // 3
	            errors.rejectValue("passwordConfirmation", "Match");
	        }      
	        if(this.appService.findByEmail(user.getEmail().toLowerCase()) != null) {
	        	errors.rejectValue("email", "DupeEmail");
	        }
	    }
}
