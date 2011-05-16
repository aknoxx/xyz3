package dst3.ejb.managed;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
	
	private String result;
	
	public LoginBean() {
		session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
		
		FacesContext fc = FacesContext.getCurrentInstance();
    	messages = fc.getApplication().getResourceBundle(fc, "m");
	}
		
	public void loginUser() {
		Query q = em.createNamedQuery("findUserByName");
		q.setParameter("username", username);
		User user = (User) q.getSingleResult();
		try {
			if(password.equals(user.getPassword())) {
				session.setAttribute("loggedIn", true);
				session.setAttribute("userId", user.getId());
				session.setAttribute("homeResult", messages.getString("home.loginSuccess"));
				setResult("");
				FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
			}
			else {
				session.setAttribute("loggedIn", false);
				setResult(messages.getString("login.wrongCredentials"));
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
