package dst3.ejb.managed;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="confirmPasswordValidator")
public class ConfirmPasswordValidator implements Validator {
	
	private ResourceBundle messages;
	
	public ConfirmPasswordValidator() {
		FacesContext fc = FacesContext.getCurrentInstance();
    	messages = fc.getApplication().getResourceBundle(fc, "vm"); 
	}

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UIInput passwordComponent = (UIInput) component.getAttributes().get("passwordComponent");
        String password = (String) passwordComponent.getValue();
        String confirmPassword = (String) value;

        if (confirmPassword != null && !confirmPassword.equals(password)) {
            throw new ValidatorException(new FacesMessage(
            		messages.getString("register.differentPasswords")));
        }
    }
}
