package dst3.ejb;

import java.util.List;

import javax.ejb.Local;

import dst3.ejb.util.*;

@Local
public interface JobManagementLocal {

	public void addJobToGridTemporary(Long gridId, int numCPUs, String workflow, List<String> params) 
					throws ComputersNotAvailableTemporaryException, 
					InvalidGridIdException;
	public void submitJobList(Long userId) throws ComputersNotAvailableException;
	public void removeTemporaryJobsFromGrid(Long gridId);
	public int getCurrentAmountOfTemporaryJobsByGrid(Long gridId);
	
}
