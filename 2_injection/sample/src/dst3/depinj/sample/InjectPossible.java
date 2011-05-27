package dst3.depinj.sample;

import dst3.depinj.annotations.*;

@Component(scope = ScopeType.PROTOTYPE)
public class InjectPossible {

	@ComponentId
	private Long id;
	
	@Inject(specificType=SimpleInterface.class, required=false)
	private SimpleInterface si;

	public void setSi(SimpleInterface si) {
		this.si = si;
	}

	public SimpleInterface getSi() {
		return si;
	}
}
