package dst3.depinj.sample;

import dst3.depinj.IInjectionController;
import dst3.depinj.InjectionController;
import dst3.depinj.InjectionException;

public class Test {

	public static void main(String[] args) {
		IInjectionController ic1 = new InjectionController();
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
		System.out.println("Singleton id: " + sInstance.getId());
		
		Prototype p1 = new Prototype();
		ic1.initialize(p1);
		p1.callSi();

		Prototype p2 = new Prototype();
		ic1.initialize(p2);
		p2.callSi();

    }
}
