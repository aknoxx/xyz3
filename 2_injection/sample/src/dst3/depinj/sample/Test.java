package dst3.depinj.sample;

import dst3.depinj.IInjectionController;
import dst3.depinj.InjectionController;
import dst3.depinj.InjectionException;

public class Test {

	public static void main(String[] args) {
		IInjectionController ic1 = new InjectionController();
		IInjectionController ic2 = new InjectionController();
		
		try {
			ic1.initialize(new NoComponent());
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized class without @Component.");
		}		
		
		ControllerWithInjections cwi = new ControllerWithInjections();
		ic1.initialize(cwi);
		
		System.out.println("Id of component: " + cwi.getId());
		cwi.callSi();
		
		ic1.initialize(new Singleton());
		
		try {
			ic1.initialize(new Singleton());
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized multiple initialization of singleton.");
		}
		
		Singleton sInstance = ic1.getSingletonInstance(Singleton.class);
		Long singletonId = sInstance.getId();
		System.out.println("Singleton id: " + singletonId);
		
		Prototype p1 = new Prototype();
		ic1.initialize(p1);
		p1.callSi();

		Prototype p2 = new Prototype();
		ic2.initialize(p2);
		p2.callSi();
		
		InjectImpossible i1 = new InjectImpossible();
		try {
			ic2.initialize(i1);
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized impossible (required) injection.");
		}
		
		InjectPossible ip = new InjectPossible();
		ic2.initialize(ip);
		if(ip.getSi() == null) {
			System.out.println("OK: Didn't inject impossible (not required) injection.");
		}
		else {
			System.out.println("ERROR");
		}
		
		WrongIdType wi = new WrongIdType();
		try {
			ic2.initialize(wi);
			System.out.println("ERROR");
		} catch (InjectionException e) {
			System.out.println("OK: Recognized wrong id type.");
		}		
		
		OtherSingleton osi = ic2.getSingletonInstance(OtherSingleton.class);
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
