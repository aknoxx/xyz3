package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.Inject;
import dst3.depinj.annotations.ScopeType;

@Component(scope = ScopeType.SINGLETON)
public class OtherSingleton {

	@ComponentId
	private Long id;

	@Inject
	public Singleton s;
	
	public void fooBar() {
		System.out.println("[OtherSingleton] id: " + 
				id + " fooBar called!");
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
