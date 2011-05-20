package dst3.depinj.sample;

import dst3.depinj.annotations.*;

@Component(scope = ScopeType.PROTOTYPE)
public class ControllerWithInjections {

	@ComponentId
	private Long id;
	
	@Inject(specificType = SimpleInterfaceImpl.class)
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
