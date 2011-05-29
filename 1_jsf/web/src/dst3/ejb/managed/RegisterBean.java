package dst3.ejb.managed;

import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
	
	private ResourceBundle messages;
	private User user;
	
	private HttpSession session;
	private FacesContext facesContext;

	public RegisterBean() {
		session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
		
		facesContext = FacesContext.getCurrentInstance();
    	messages = facesContext.getApplication().getResourceBundle(facesContext, "m");

		user = new User();
		user.setAddress(new Address());
	}
	
	public void registerUser() {
		
		Query q = em.createNamedQuery("findUserByName");
        q.setParameter("username", user.getUsername());        
        
        // check if username already exists
        if (q.getResultList().size() != 0) {
        	FacesContext context = FacesContext.getCurrentInstance();
        	
        	FacesMessage message = new FacesMessage();
        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
        	message.setSummary("Username already exists.");
        	message.setDetail("Username already exists.");
        	
        	context.addMessage("registerForm:username", message);
        	return;
        }
		
        // create new user
		try {
			ut.begin();
			em.persist(user);		
			ut.commit();
			
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.setKeepMessages(true);
			flash.put("responseMsg", messages.getString("home.registerSuccess"));
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
			
			/*FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			message.setSummary(messages.getString("home.registerSuccess"));
        	message.setDetail(messages.getString("home.registerSuccess"));*/
        	//context.addMessage("homeForm:result", message);
				
		} catch (Exception e) {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("responseMsg", messages.getString("register.errorRegisteringUser"));
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
