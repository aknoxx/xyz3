package dst3.ejb.managed;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import dst3.ejb.dto.SearchResultDto;

@ManagedBean
@SessionScoped
public class SearchBean {
	
	private String gridName;
	private SearchResultDto results;
	
	public void search() {
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("result.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridName() {
		return gridName;
	}

	public void setResults(SearchResultDto results) {
		this.results = results;
	}

	public SearchResultDto getResults() {
		return results;
	}
}
