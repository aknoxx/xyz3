package dst3.depinj;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dst3.depinj.annotations.*;

public class InjectionController implements IInjectionController {
	
	private static Long uniqueId = new Long(0);	
	private static Map<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();

	@Override
	public synchronized void initialize(Object obj) throws InjectionException {
		initialize(obj, false);		
	}
	
	private Object initialize(Object obj, boolean injecting) {
		ScopeType scope;
		if(obj.getClass().isAnnotationPresent(Component.class)) {
			Component c = obj.getClass().getAnnotation(Component.class);
			scope = c.scope();
		}
		else {
			throw new InjectionException("Component annotation missing in class " 
					+ obj.getClass().getName());
		}		
		
		Object objToInject = null;
		boolean hasId = false;
		Field idField = null;
		List<Field> fields = new ArrayList<Field>();
		Collections.addAll(fields, obj.getClass().getDeclaredFields());

		for (Field field : fields) {
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
						injClassType = field.getType();
					}
					else {
						injClassType = inj.specificType();						
					}
					
					if(singletons.containsKey(injClassType)) {
						objToInject = singletons.get(injClassType);
					}
					
					Component injC = injClassType.getAnnotation(Component.class);
					if(injC == null) {
						if(inj.required()) {
							throw new InjectionException("Impossible to inject required object in class " 
								+ obj.getClass().getName());
						}
					}
					else {
						if(objToInject == null) {
							try {
								@SuppressWarnings("unchecked")
								Class<Object> fc = (Class<Object>) Class.forName(injClassType.getName());
								objToInject = fc.newInstance();
							} catch (InstantiationException e) {
								throw new InjectionException(e);
							} catch (IllegalAccessException e) {
								throw new InjectionException(e);
							} catch (ClassNotFoundException e) {
								throw new InjectionException(e);
							}
							initialize(objToInject, true);
						}
						
						if(objToInject == null && inj.required()) {
							throw new InjectionException("Impossible to inject required object in class " 
								+ obj.getClass().getName());
						}

						if (!Modifier.isPublic(field.getModifiers())) {
							field.setAccessible(true);
				        }
				        try {
				        	field.set(obj, objToInject);
				        	
				        } catch (IllegalAccessException iae) {
				        		throw new InjectionException(iae);
				        }
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
				return singletons.get(obj.getClass());
			}
			else {
				singletons.put(obj.getClass(), obj);
				return obj;
			}
		}
		return objToInject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSingletonInstance(Class<T> clazz) throws InjectionException {
		
		if(!singletons.containsKey(clazz)) {
			Object singleton;
			try {
				Class<Object> fc = (Class<Object>) Class.forName(clazz.getName());
				singleton = fc.newInstance();
			} catch (InstantiationException e) {
				throw new InjectionException(e);
			} catch (IllegalAccessException e) {
				throw new InjectionException(e);
			} catch (ClassNotFoundException e) {
				throw new InjectionException(e);
			}
			initialize(singleton);
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
