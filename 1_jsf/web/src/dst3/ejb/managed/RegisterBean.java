package dst3.ejb.managed;

import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import dst3.ejb.model.*;

@ManagedBean
@SessionScoped
public class RegisterBean {
	
	@PersistenceContext
	EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	@Inject
	private HomeBean homeBean;
	
	private ResourceBundle messages;
	
	private String result = "";
	private User user;
	
	private HttpSession session;

	public RegisterBean() {
		session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
		
		FacesContext fc = FacesContext.getCurrentInstance();
    	messages = fc.getApplication().getResourceBundle(fc, "m");

		user = new User();
		user.setAddress(new Address());
	}
	
	public void registerUser() {
		try {
			ut.begin();
			em.persist(user);		
			ut.commit();	

			session.setAttribute("homeResult", messages.getString("home.registerSuccess"));
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
		} catch (Exception e) {
			setResult(messages.getString("register.errorRegisteringUser"));
		}
	}
	
	public void validateConfirmPassword(FacesContext context, UIComponent toValidate, Object value) {
	    String confirmPassword = (String)value;
	    if (!confirmPassword.equals(user.getPassword())) {
	      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	    		  "Passwords do not match!", "Passwords do not match!");
	      throw new ValidatorException(message);
	    }
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
