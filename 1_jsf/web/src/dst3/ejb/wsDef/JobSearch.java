package dst3.ejb.wsDef;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst3.ejb.dto.SearchResultDto;
import dst3.ejb.model.Execution;
import dst3.ejb.model.Job;
import dst3.ejb.model.JobStatus;
import dst3.ejb.model.User;

@WebService
public class JobSearch {
	
	@PersistenceContext
	EntityManager em;

    public JobSearch() {}

    @WebMethod
    public List<SearchResultDto> search(String gridName) {    	
    	Query q = em.createNamedQuery("findExecutionsByStatus");
		q.setParameter("status", JobStatus.SCHEDULED);
		List<Execution> executions = q.getResultList();
		
		List<SearchResultDto> results = new ArrayList<SearchResultDto>();
		for (Execution execution : executions) {
			Job job = execution.getJob();
			User user = job.getUser();
			results.add(new SearchResultDto(job.getId(), execution.getStart(), 
					execution.getEnd(), user.getUsername()));
		}
		return results;
    }
}