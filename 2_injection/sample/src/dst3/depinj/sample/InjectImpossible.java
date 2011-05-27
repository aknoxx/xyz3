package dst3.depinj.sample;

import dst3.depinj.annotations.*;

@Component(scope = ScopeType.PROTOTYPE)
public class InjectImpossible {

	@ComponentId
	private Long id;
	
	@Inject
	private SimpleInterface si;
}
