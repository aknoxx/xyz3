package dst3.ejb.managed;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

import dst3.ejb.ws.JobSearch;
import dst3.ejb.ws.JobSearchService;
//import dst3.ws.JobSearch;
//import dst3.ws.JobSearchService;
import dst3.ejb.ws.SearchResultDto;


@ManagedBean
@SessionScoped
public class SearchBean {
	
	private String gridName;
	
	/**
	 * search.xhtml
	 */
	public void search() {
		
		JobSearchService service = new JobSearchService();
		 
		JobSearch port = service.getJobSearchPort();
		List<SearchResultDto> results = port.search(getGridName());
		
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.put("searchResults", results);		

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("result.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridName() {
		return gridName;
	}
}
