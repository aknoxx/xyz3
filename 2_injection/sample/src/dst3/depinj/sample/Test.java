package dst3.depinj.sample;

import dst3.depinj.IInjectionController;
import dst3.depinj.InjectionController;
import dst3.depinj.InjectionException;

public class Test {

	public static void main(String[] args) {
		IInjectionController ic = InjectionController.getInstance();		
		try {
			ic.initialize(new NoComponent());
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized class without @Component.");
		}		
		
		ControllerWithInjections cwi = new ControllerWithInjections();
		ic.initialize(cwi);
		
		System.out.println("Id of component: " + cwi.getId());
		cwi.callSi();
		
		ic.initialize(new Singleton());
		
		try {
			ic.initialize(new Singleton());
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized multiple initialization of singleton.");
		}
		
		Singleton sInstance = ic.getSingletonInstance(Singleton.class);
		Long singletonId = sInstance.getId();
		System.out.println("Singleton id: " + singletonId);
		
		Prototype p1 = new Prototype();
		ic.initialize(p1);
		p1.callSi();

		Prototype p2 = new Prototype();
		ic.initialize(p2);
		p2.callSi();
		
		InjectImpossible i1 = new InjectImpossible();
		try {
			ic.initialize(i1);
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized impossible (required) injection.");
		}
		
		InjectPossible ip = new InjectPossible();
		ic.initialize(ip);
		if(ip.getSi() == null) {
			System.out.println("OK: Didn't inject impossible (not required) injection.");
		}
		else {
			System.out.println("ERROR");
		}
		
		WrongIdType wi = new WrongIdType();
		try {
			ic.initialize(wi);
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized wrong id type.");
		}		
		
		OtherSingleton osi = ic.getSingletonInstance(OtherSingleton.class);
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
