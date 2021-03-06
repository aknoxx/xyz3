package dst3.ejb.managed;

import java.io.IOException;
import java.util.List;

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
public class OverviewBean {

	@PersistenceContext
	EntityManager em;
	
	private HttpSession session;
	private boolean loggedIn = false; 
    
    // called before page is rendered
    public void initLogin() {
    	if(session.getAttribute("loggedIn") == null) {
    		setLoggedIn(false);
    		session.setAttribute("loggedIn", false);
    	}
    	else {
    		boolean state = (Boolean) session.getAttribute("loggedIn");
    		setLoggedIn(state);
    	}
    }
    
    public void logoutUser() {
    	session.setAttribute("loggedIn", false);
    	setLoggedIn(false);
    }

	public OverviewBean() {
		session = (HttpSession) FacesContext.getCurrentInstance()
			.getExternalContext().getSession(true);
	}

	public void addJob(Long gridId) {
		
		session.setAttribute("gridId", gridId); 
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("job.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Grid> getGrids() {
		Query q = em.createQuery("SELECT g FROM Grid g");
		return (List<Grid>) q.getResultList();
	}
	
	public int getAmountOfFreeCPUsByGrid(Long gridId) {		
		Query query = em.createNamedQuery("findAllComputersByGrid");
		query.setParameter("gridId", gridId);
		List<Computer> allComputersOfGrid = query.getResultList();
		int allCPUs = 0;
		for (Computer computer : allComputersOfGrid) {
			allCPUs += computer.getCpus();
		}
		
		query = em.createNamedQuery("findUsedComputersByGrid1");
		query.setParameter("statusRunning", JobStatus.RUNNING);
		query.setParameter("statusScheduled", JobStatus.SCHEDULED);
		query.setParameter("gridId", gridId);
		List<Computer> usedComputersOfGrid = query.getResultList();
		int usedCPUs = 0;
		for (Computer computer : usedComputersOfGrid) {
			usedCPUs += computer.getCpus();
		}
		return allCPUs - usedCPUs;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
}
