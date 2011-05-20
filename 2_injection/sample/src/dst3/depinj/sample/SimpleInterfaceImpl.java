package dst3.depinj.sample;

import dst3.depinj.annotations.*;

@Component(scope = ScopeType.SINGLETON)
public class SimpleInterfaceImpl implements SimpleInterface {

	@ComponentId
	private Long id;
	
	@Override
	public void fooBar() {
		System.out.println("[SimpleInterfaceImpl] id: " + 
				id + " fooBar called!");
	}
	
	public Long getId() {
		return id;
	}
}
