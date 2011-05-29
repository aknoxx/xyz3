package dst3.ejb.dto;

import java.io.Serializable;
import java.util.Date;

public class SearchResultDto implements Serializable {

	private static final long serialVersionUID = 6012688672445109077L;
	
	public Long jobId;
	public Date start;
	public Date finish;
	public String username;
	
	public SearchResultDto() { }
	
	public SearchResultDto(Long jobId, Date start, Date finish, String username) {
		super();
		this.jobId = jobId;
		this.start = start;
		this.finish = finish;
		this.username = username;
	}
}
