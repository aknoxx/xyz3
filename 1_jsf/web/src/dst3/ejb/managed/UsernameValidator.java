package dst3.ejb.managed;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@FacesValidator(value="usernameValidator")
public class UsernameValidator implements Validator {
	
	@PersistenceContext
	EntityManager em;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String username = (String) value;

        /*
        Query q = em.createNamedQuery("findUserByName");
        q.setParameter("username", username);        
        
        if (q.getSingleResult() != null) {
            throw new ValidatorException(new FacesMessage(
                "Username already exists."));
        }*/
    }
}
