package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.Inject;
import dst3.depinj.annotations.ScopeType;

@Component(scope = ScopeType.PROTOTYPE)
public class Prototype {

	@ComponentId
	private Long id;
	
	@Inject(specificType = SimpleInterfaceImpl.class, required = true)
	private SimpleInterface si;
	
	public void callSi() {
		si.fooBar();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
