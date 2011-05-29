package dst3.ejb.managed;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import dst3.ejb.model.*;

@ManagedBean
@SessionScoped
public class LoginBean {
	
	@PersistenceContext
	EntityManager em;
	
	private HttpSession session;

	private String username;
	private String password;
	
	private ResourceBundle messages;
	
	public LoginBean() {
		session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
		
		FacesContext fc = FacesContext.getCurrentInstance();
    	messages = fc.getApplication().getResourceBundle(fc, "m");
	}
		
	public void loginUser() {
		Query q = em.createNamedQuery("findUserByName");
		q.setParameter("username", username);
		
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.setKeepMessages(true);
		
		try {
			User user = (User) q.getSingleResult();
			
			if(password.equals(user.getPassword())) {
				session.setAttribute("loggedIn", true);
				session.setAttribute("userId", user.getId());
				
				flash.put("responseMsg", messages.getString("home.loginSuccess"));
			}
			else {
				session.setAttribute("loggedIn", false);
				flash.put("responseMsg", messages.getString("login.wrongCredentials"));
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
		} catch (Exception e) {
			session.setAttribute("loggedIn", false);
			flash.put("responseMsg", messages.getString("login.wrongCredentials"));
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
			} catch (IOException e1) {
			}
		}
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
}
