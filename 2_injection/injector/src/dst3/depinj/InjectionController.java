package dst3.depinj;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import dst3.depinj.annotations.*;

public class InjectionController implements IInjectionController {
	
	private static Long uniqueId = new Long(0);	
	private static Map<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();

	@Override
	public synchronized void initialize(Object obj) throws InjectionException {
		
		//Annotation[] annos = obj.getClass().getAnnotations();
		initialize(obj, false);
		
	}
	
	private void initialize(Object obj, boolean injecting) {
		ScopeType scope;
		if(obj.getClass().isAnnotationPresent(Component.class)) {
			Component c = obj.getClass().getAnnotation(Component.class);
			scope = c.scope();
		}
		else {
			throw new InjectionException("Component annotation missing in class " 
					+ obj.getClass().getName());
		}		
		
		boolean hasId = false;
		Field idField = null;
		for (Field field : obj.getClass().getDeclaredFields()) {
			ComponentId cId = field.getAnnotation(ComponentId.class);
			if(cId != null) {
				if(hasId) {
					throw new InjectionException("More than one ComponentId annotation in class " 
							+ obj.getClass().getName());
				}				
				if(field.getType() != Long.class) {
					throw new InjectionException("Non-Long field annotated with ComponentId in class " 
							+ obj.getClass().getName());
				}
				hasId = true;
				idField = field;
			}
			else {
				Inject inj = field.getAnnotation(Inject.class);
				if(inj != null) {
					Class<?> injClassType;
					if(inj.specificType() == Object.class) {
						//injClassType = obj.getClass();
						injClassType = field.getType();
					}
					else {
						injClassType = inj.specificType();
					}					
					
					Component injC = injClassType.getAnnotation(Component.class);
					if(injC == null) {
						throw new InjectionException("Impossible to inject required object in class " 
								+ obj.getClass().getName());
					}
					
					ScopeType injScopeType = injC.scope();
					
					Object myFruit = null;
					if(injScopeType.equals(ScopeType.SINGLETON)) {
						if(singletons.containsKey(injClassType)) {
							myFruit = singletons.get(injClassType);
						}
						//singletons.put(obj.getClass(), obj);
					}
					
					if(myFruit == null) {
						try {
							@SuppressWarnings("unchecked")
							Class<Object> fc = (Class<Object>) Class.forName(injClassType.getName());
							myFruit = fc.newInstance();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//if(scope.equals(ScopeType.SINGLETON)) {
						//	singletons.put(injClassType, myFruit);
						//}
						initialize(myFruit, true);
					}
					
					Object objToInj = null;
					//try {
						//objToInj = injClassType.newInstance();
						//initialize(myFruit, true);
						
					/*} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					
					/*Component injC = injClassType.getAnnotation(Component.class);
					if(injC == null) {
						throw new InjectionException("Impossible to inject required object in class " 
								+ obj.getClass().getName());
					}
					
					ScopeType injScopeType = injC.scope();					
					*/
					/*
					Object objToInj = null;
					if(injScopeType.equals(ScopeType.SINGLETON)) {
						if(!singletons.containsKey(injClassType)) {
							singletons.put(injClassType, injObj);
						}
					}					
					else {						
						try {
							objToInj = injClassType.newInstance();
						} catch (Exception e) {
							if(inj.required()) {
								throw new InjectionException("Impossible to inject required object in class " 
										+ obj.getClass().getName());
							}
						}
					}			*/		
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
			        }
			        try {
			        	field.set(obj, myFruit);
			        	
			        } catch (IllegalAccessException iae) {
			        		throw new InjectionException(iae);
			        }					
				}
			}
		}

		if(!hasId) {
			throw new InjectionException("Class " + obj.getClass().getName() 
					+ " misses ComponentId annotation.");
		}

		if (!Modifier.isPublic(idField.getModifiers())) {
			idField.setAccessible(true);
        }
        try {
        	idField.set(obj, getUniqueId());
        } catch (IllegalAccessException iae) {
            throw new InjectionException(iae);
        }
		
		if(scope.equals(ScopeType.SINGLETON)) {
			if(singletons.containsKey(obj.getClass())) {
				if(!injecting) {
					throw new InjectionException("Singleton cannot be injected more than once in class " 
							+ obj.getClass().getName());
				}
			}
			else {
				singletons.put(obj.getClass(), obj);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSingletonInstance(Class<T> clazz) throws InjectionException {
		if(!singletons.containsKey(clazz)) {
			throw new InjectionException("Instance of specified class does not exist.");
		}
		return (T) singletons.get(clazz);
	}

	private Long getUniqueId() {
		uniqueId++;
		return uniqueId;
	}
	
	public int getSize() {
		return singletons.size();
	}
}
