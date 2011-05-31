package dst3.depinj.sample;

import dst3.depinj.InjectionController;
import dst3.depinj.InjectionException;

public class TestAgent {

	public static void main(String[] args) {
		
		ControllerWithInjections cwi = new ControllerWithInjections();		
		System.out.println("Id of component: " + cwi.getId());
		cwi.callSi();
		
		Singleton s = new Singleton();		
		System.out.println("Id of component: " + s.getId());
		
		try {
			new Singleton();
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized multiple initialization of singleton.");
		}
		
		Singleton sInstance = InjectionController.getInstance()
				.getSingletonInstance(Singleton.class);
		Long singletonId = sInstance.getId();
		System.out.println("Singleton id: " + singletonId);
		
		Prototype p1 = new Prototype();
		p1.callSi();

		Prototype p2 = new Prototype();
		p2.callSi();
		
		try {
			new InjectImpossible();
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized impossible (required) injection.");
		}
		
		InjectPossible ip = new InjectPossible();
		if(ip.getSi() == null) {
			System.out.println("OK: Didn't inject impossible (not required) injection.");
		}
		else {
			System.out.println("ERROR");
		}

		try {
			new WrongIdType();
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized wrong id type.");
		}		
		
		OtherSingleton osi = new OtherSingleton();
		osi.fooBar();
		osi.s.fooBar();
		if(singletonId == osi.s.getId()) {
			System.out.println("OK: Singleton reused.");
		}
		else {
			System.out.println("ERROR");
		}
	}
}
