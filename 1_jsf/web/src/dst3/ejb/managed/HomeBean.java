package dst3.ejb.managed;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import dst3.ejb.model.*;

@ManagedBean
@SessionScoped
public class HomeBean {
	
	@PersistenceContext
	EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	private ResourceBundle messages;
	
	private HttpSession session;
    
    private String result = "";
    private boolean disabledAdd = false;
    private boolean disabledRemove = false;
    private boolean loggedIn = false; 
    
    // called before page is rendered
    public void initLogin() {
    	if(session.getAttribute("loggedIn") == null) {
    		setLoggedIn(false);
    		session.setAttribute("loggedIn", false);
    	}
    	else {
    		boolean state = (Boolean) session.getAttribute("loggedIn");
    		session.setAttribute("loggedIn", state);
    		setLoggedIn(state);
    	}
    }
    
    public void logoutUser() {
    	session.setAttribute("loggedIn", false);
    	session.setAttribute("userId", null);
    	setLoggedIn(false);
    }

    public HomeBean() {
    	
    	session = (HttpSession) FacesContext.getCurrentInstance()
		.getExternalContext().getSession(true);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
    	messages = fc.getApplication().getResourceBundle(fc, "m"); 
    }
    
    public void addTestData() {
    	try {
			ut.begin();

	    	Grid grid1 = new Grid("grid1", "location1", new BigDecimal(1));
			Grid grid2 = new Grid("grid2", "location2", new BigDecimal(2));	
			em.persist(grid1);
			em.persist(grid2);
			
			Admin admin1 = new Admin("firstName1", "lastname1", new Address("street1", "city1", "zipCode1"));
			Admin admin2 = new Admin("firstName2", "lastname2", new Address("street2", "city2", "zipCode2"));
			em.persist(admin1);
			em.persist(admin2);
			
			Cluster cluster1 = new Cluster("name1", new Date(), new Date());
			cluster1.setAdmin(admin1);
			cluster1.setGrid(grid1);
			em.persist(cluster1);
			grid1.getClusters().add(cluster1);
			em.merge(grid1);		
			
			Cluster cluster2 = new Cluster("name2", new Date(), new Date());
			cluster2.setAdmin(admin2);
			cluster2.setGrid(grid2);	
			em.persist(cluster2);
			grid2.getClusters().add(cluster2);
			em.merge(grid2);
			
			for (int i = 0; i < 2; i++) {
				Computer computer = new Computer("name"+i+1, 2, "location"+i+1, new Date(), new Date());
				computer.setCluster(cluster1);
				cluster1.getComputers().add(computer);
				em.persist(computer);
			}
			em.merge(cluster1);
			
			for (int i = 2; i < 5; i++) {
				Computer computer = new Computer("name"+i+1, 4, "location"+i+1, new Date(), new Date());
				computer.setCluster(cluster2);
				cluster2.getComputers().add(computer);
				em.persist(computer);
			}
			em.merge(cluster2);
		
			ut.commit();
			
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("responseMsg", messages.getString("home.addedTestData"));
			
			disabledAdd = true;
			disabledRemove = false;
		} catch (Exception e) {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("responseMsg", messages.getString("home.errorAddData"));
		}
    }
    
    public void removeAllData() {
    	try {
			ut.begin();
		
			Query q = em.createQuery("SELECT c FROM Computer c");
			List<Computer> computers = q.getResultList();
			for (Computer c : computers) {
				em.remove(c);
			}
			
			q = em.createQuery("SELECT c FROM Cluster c");
			List<Cluster> clusters = q.getResultList();
			for (Cluster c : clusters) {
				em.remove(c);
			}
			
			q = em.createQuery("SELECT a FROM Admin a");
			List<Admin> admins = q.getResultList();
			for (Admin a : admins) {
				em.remove(a);
			}
			
			q = em.createQuery("SELECT g FROM Grid g");
			List<Grid> grids = q.getResultList();
			for (Grid g : grids) {
				em.remove(g);
			}
			
			q = em.createQuery("SELECT j FROM Job j");
			List<Job> jobs = q.getResultList();
			for (Job j : jobs) {
				em.remove(j);
			}
			
			q = em.createQuery("SELECT u FROM User u");
			List<User> users = q.getResultList();
			for (User u : users) {
				em.remove(u);
			}
		
			ut.commit();
			
			disabledAdd = false;
			disabledRemove = true;
			
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("responseMsg", messages.getString("home.removedTestData"));
		
		} catch (Exception e) {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("responseMsg", messages.getString("home.errorRemoveData"));
		}
    }

	public void setDisabledAdd(boolean disabledAdd) {
		this.disabledAdd = disabledAdd;
	}

	public boolean getDisabledAdd() {
		return disabledAdd;
	}

	public void setDisabledRemove(boolean disabledRemove) {
		this.disabledRemove = disabledRemove;
	}

	public boolean getDisabledRemove() {
		return disabledRemove;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
}
