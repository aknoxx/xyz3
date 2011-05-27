package dst3.depinj.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import dst3.depinj.annotations.Component;

public class SimpleTransformer implements ClassFileTransformer {
	 
	public SimpleTransformer() {
		super();
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		String[] ignore = new String[] { "sun/", "java/", "javax/" };
		
		for (int i = 0; i < ignore.length; i++) {
		    if (className.startsWith(ignore[i])) {
		        return classfileBuffer;
		    }
		}
		
		return doClass(className, classBeingRedefined, classfileBuffer);
	}
	
	private byte[] doClass(String className, Class<?> clazz, byte[] b) {
	    ClassPool pool = ClassPool.getDefault();
	    CtClass cl = null;
	    /*		
		//if(clazz.isAnnotationPresent(Component.class)) {
			
		if(clazz.getAnnotation(Component.class) != null) {
	  
		    try {		    	
			    	//System.out.println("Transformer to Transform Class: " + className);
			    	
			      cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
			      
			     // CtConstructor[] ms = cl.getConstructors();
			      
			      //System.out.println("constructors: " + ms.length); 
			      
			      //for (CtConstructor ctConstructor : ms) {
			    	  //ctConstructor.insertBefore("{ new InjectionController().initialize(this); }");
			    	  //System.out.println(ms.toString());
			      //}
		
			      // cc.writeFile();
			      
			      b = cl.toBytecode();
		    } catch (Exception e) {
		      System.err.println("Could not instrument  " + className
		          + ",  exception : " + e.getMessage());
		    } finally {
			      if (cl != null) {
			        cl.detach();
			      }
		    }
		}*/
	    
	    return b;
	  }
}
