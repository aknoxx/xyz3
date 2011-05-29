package dst3.ejb.managed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import dst3.ejb.JobManagementLocal;
import dst3.ejb.model.*;
import dst3.ejb.util.ComputersNotAvailableException;
import dst3.ejb.util.ComputersNotAvailableTemporaryException;
import dst3.ejb.util.InvalidGridIdException;

@ManagedBean
@SessionScoped
public class JobBean {

	@PersistenceContext
	EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	@EJB
	JobManagementLocal jm;
	
	private ResourceBundle messages;
	
	private Long gridId;
	private int numCPUs;
	private String workflow;
	private String param1;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	
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
	
	public JobBean() {
		session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
		
		FacesContext fc = FacesContext.getCurrentInstance();
    	messages = fc.getApplication().getResourceBundle(fc, "m");
	}
	
	/*
	 * Methods for assignment.xhtml
	 */

	public List<Grid> getGrids() {
		Query q = em.createQuery("SELECT g FROM Grid g");
		return (List<Grid>) q.getResultList();
	}
	
	public int getNoOfJobs(Long gridId) {
		return jm.getCurrentAmountOfTemporaryJobsByGrid(gridId);
	}
	
	public void removeTempJobs(Long gridId) {
		jm.removeTemporaryJobsFromGrid(gridId);
	}
	
	public void submitJobAssignments() {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		
		if(loggedIn) {
			Long userId = (Long) session.getAttribute("userId");			
			
			try {
				jm.submitJobList(userId);
				try {					
					flash.put("responseMsg", messages.getString("home.submissionSuccess"));	
					flash.setKeepMessages(true);
					FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (ComputersNotAvailableException e) {
				flash.put("responseMsg", messages.getString("assignment.notEnoughComputers"));
			}
		}
		else {
			flash.put("responseMsg", messages.getString("assignment.loginNeeded"));
		}
	}
	
	/*
	 * Methods for job.xhtml
	 */
	
	public void loadGridId() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
		setGridId((Long) session.getAttribute("gridId"));
	}
	
	public void addJobToCache() {
		List<String> params = new ArrayList<String>();
		if(param1 != "")
			params.add(param1);
		if(param2 != "")
			params.add(param2);
		if(param3 != "")
			params.add(param3);
		if(param4 != "")
			params.add(param4);
		if(param5 != "")
			params.add(param5);		
		
		try {
			jm.addJobToGridTemporary(gridId, numCPUs, workflow, params);
			try {
				resetForm();
				FacesContext.getCurrentInstance().getExternalContext().redirect("overview.jsf");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ComputersNotAvailableTemporaryException e) {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("responseMsg", messages.getString("job.notEnoughCPUs"));
		} catch (InvalidGridIdException e) {
			// not possible here
		}
	}
	
	private void resetForm() {
		gridId = null;
		numCPUs = 0;
		workflow = null;
		param1 = null;
		param2 = null;
		param3 = null;
		param4 = null;
		param5 = null;
	}
	
	
	public ResourceBundle getMessages() {
		return messages;
	}

	public void setMessages(ResourceBundle messages) {
		this.messages = messages;
	}
	
	public Long getGridId() {
		return gridId;
	}
	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
	public int getNumCPUs() {
		return numCPUs;
	}
	public void setNumCPUs(int numCPUs) {
		this.numCPUs = numCPUs;
	}
	public String getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
}
