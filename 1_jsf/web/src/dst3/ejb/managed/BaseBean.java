package dst3.ejb.managed;

import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public abstract class BaseBean {

	@PersistenceContext
	EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	public ResourceBundle messages;
	
	public BaseBean() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
						.getExternalContext().getSession(true);
		messages = (ResourceBundle)session.getAttribute("messages");
	}
}
