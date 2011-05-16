package dst3.ejb.managed;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

	// <s:validateForm fields="passwordId confirmPasswordId" validatorId="passwordValidator" />
	
	// check if user already exists -> username
	
	//@Inject
	//@InputField
	private String password;
	
	//@Inject
	//@InputField
	private String confirmPassword;

	@Override
	public void validate(final FacesContext context, final UIComponent comp, final Object values)
			throws ValidatorException {
		if(!password.equals(confirmPassword)) {
			 throw new ValidatorException(
					 new FacesMessage("Sorry, that location is not in our database. Please try again."));
		}		
	}
}
