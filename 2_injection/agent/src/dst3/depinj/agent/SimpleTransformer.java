package dst3.depinj.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

import javassist.*;
import dst3.depinj.InjectionController;
import dst3.depinj.annotations.Component;

public class SimpleTransformer implements ClassFileTransformer {
	 
	public SimpleTransformer() {
		super();
	}

	@Override
	public byte[] transform(ClassLoader loader, String pack, Class<?> clazz, 
			ProtectionDomain domain, byte[] byteArray)
			throws IllegalClassFormatException {

		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get(pack.replace("/", "."));
			
			if (cc.getAnnotation(Component.class) != null) {

				String icName = InjectionController.class.getCanonicalName();
					
				CtClass icClass = cp.get(icName);
				// inject the InjectionController by adding a field to the classes
				CtField field = new CtField(icClass, "ic", cc);
				field.setModifiers(Modifier.PUBLIC);
				cc.addField(field);
				
				CtConstructor[] cons = cc.getConstructors();
				for (CtConstructor con : cons) {
			    	  con.insertAfter("{ ic = " + icName + ".getInstance(); ic.initialize(this); }");
			    }
				return cc.toBytecode();
			}
		}catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			// classes without @Component annotation aren't processed
		}
		return null;
	}
}
