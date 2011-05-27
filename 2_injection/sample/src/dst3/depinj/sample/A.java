package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.ScopeType;

@Component(scope = ScopeType.PROTOTYPE)
public class A {

	@ComponentId
	private int id;
	
	
}
