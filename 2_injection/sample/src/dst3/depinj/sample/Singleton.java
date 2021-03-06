package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.ScopeType;

@Component(scope = ScopeType.SINGLETON)
public class Singleton {

	@ComponentId
	private Long id;
	
	public Singleton() {
		
	}

	public void fooBar() {
		System.out.println("[Singleton] id: " + 
				id + " fooBar called!");
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
