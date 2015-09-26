package ru.ncedu.java.tasks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ReflectionsImpl implements Reflections {

	@Override
	public Object getFieldValueByName(Object object, String fieldName)
			throws NoSuchFieldException {
		Class<?> clazz = object.getClass();
		Field f = clazz.getDeclaredField(fieldName);
		f.setAccessible(true);
		try {
			return f.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("wrong arg of getFieldValueByName func");
		}
	}

	@Override
	public Set<String> getProtectedMethodNames(Class clazz) {
		Set<String> ret = new HashSet<String>();
		Method[] methods = clazz.getDeclaredMethods();
		for(Method x: methods)
			if(Modifier.isProtected(x.getModifiers()))
				ret.add(x.getName());
		return ret;
	}

	@Override
	public Set<Method> getAllImplementedMethodsWithSupers(Class clazz) {
		Set<Method> ret = new HashSet<Method>();
		
		Method[] methods = clazz.getDeclaredMethods();
		for(Method x: methods)
			ret.add(x);
		
		if(clazz.getSuperclass() == null){
			return ret;
		}else{
			Set<Method> tmp = getAllImplementedMethodsWithSupers(clazz.getSuperclass());
			for(Method x: tmp)
				ret.add(x);
			return ret;
		}
	
		
	}

	@Override
	public List<Class> getExtendsHierarchy(Class clazz) {
		List<Class> ret = new LinkedList<Class>();
		
		Class superclazz = clazz.getSuperclass();
		
		if(superclazz == null){
			return ret;
		}else{
			ret.add(superclazz);
			List<Class> tmp = getExtendsHierarchy(superclazz);
			for(Class x: tmp)
				ret.add(x);
			return ret;
		}
	}

	@Override
	public Set<Class> getImplementedInterfaces(Class clazz) {
		Set<Class> ret = new HashSet<Class>();
		
		Class[] interfs = clazz.getInterfaces();
		for(Class x: interfs)
			ret.add(x);
		return ret;
	}

	@Override
	public List<Class> getThrownExceptions(Method method) {
		Class[] exspt = method.getExceptionTypes();
		List<Class> ret = new LinkedList<Class>();
		for(Class x: exspt)
			ret.add(x);
		return ret;
	}

	@Override
	public String getFooFunctionResultForDefaultConstructedClass() {
//		Class<?> clazz = Class.forName("ru.ncedu.java.tasks.Reflections$SecretClass");
		Class<?> clazz;
		try {
			clazz = Class.forName("ru.ncedu.java.tasks.Reflections");
			clazz = clazz.getClasses()[0];
			
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class<?>[0]);
			constructor.setAccessible(true);
			
			Object secretClassInstance = constructor.newInstance(new Object[0]);
			
			Method method = clazz.getDeclaredMethod("foo", new Class<?>[0]);
			
			method.setAccessible(true);
			
			String result = (String) method.invoke(secretClassInstance, new Object[0]);
			
			return result;
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Class was not found", e); 
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Method or constructor was not found", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("Method is private", e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Constructor error", e);
		}
	}

	@Override
	public String getFooFunctionResultForClass(String constructorParameter,
			String string, Integer... integers) {
//		Class<?> clazz = Class.forName("ru.ncedu.java.tasks.Reflections$SecretClass");
		Class<?> clazz;
		try {
			clazz = Class.forName("ru.ncedu.java.tasks.Reflections");
			clazz = clazz.getClasses()[0];
			
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class<?>[] {String.class});
			constructor.setAccessible(true);
			
			Object secretClassInstance = constructor.newInstance(new Object[] {constructorParameter});
				
			Method method = clazz.getDeclaredMethod("foo", new Class<?>[] {String.class, Integer[].class});
			
			method.setAccessible(true);
			
			String result = (String) method.invoke(secretClassInstance, new Object[] {string, integers});
			
			return result;
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Class was not found", e); 
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Method or constructor was not found", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("Method is private", e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Constructor error", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Constructor error", e);
		}
	}

	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
