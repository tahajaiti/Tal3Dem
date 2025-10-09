package org.kyojin.core;

import org.kyojin.core.exception.ClassCreationException;
import org.kyojin.core.exception.InjectionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class Injector {
    private static final Map<Class<?>, Object> singletonRegistry = new HashMap<>();
    private static final Map<Class<?>, Injectable.Scope> scopeRegistry = new HashMap<>();
    private static final Map<Class<?>, Class<?>> interfaceBindings = new HashMap<>();
    private static final List<String> excludedPackages = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Injector.class);

    private Injector() {}


    /**
     * Scans the specified package and its sub-packages for classes annotated with @Injectable
     * and registers them in the Injector.
     * @param packageName the base package to scan for @Injectable classes
     */
    public static void scanPackage(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            URL folderPath = Thread.currentThread().getContextClassLoader().getResource(path);
            if (folderPath == null) throw new IllegalArgumentException("Package not found: " + packageName);

            File dir = new File(folderPath.toURI());
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (excludedPackages.stream().anyMatch(packageName::startsWith)) continue;

                if (file.isDirectory()) {
                    scanPackage(packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    Class<?> cls = Class.forName(className);

                    if (cls.isAnnotationPresent(Injectable.class)) {
                        register(cls);
                        logger.info("Registered injectable class: {}", className);
                    }

                    if (cls.isAnnotationPresent(Implementation.class)) {
                        Implementation implAnnotation = cls.getAnnotation(Implementation.class);
                        Class<?> iface = implAnnotation.value();
                        bind(iface, cls);
                        logger.info("Bound interface {} -> {}", iface.getName(), cls.getName());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to scan package {}: {}", packageName, e.getMessage(), e);
            throw new RuntimeException("Failed to scan package: " + packageName);
        }
    }

    /**
     * Excludes a package from being scanned by the Injector.
     * @param packageName the package name to exclude
     */
    public static void excludePackage(String packageName) {
        excludedPackages.add(packageName);
    }

    /**
     * Excludes multiple packages from being scanned by the Injector.
     * @param packageNames the list of package names to exclude
     */
    public static void excludePackage(List<String> packageNames) {
        excludedPackages.addAll(packageNames);
    }

    /**
     * Registers a class annotated with @Injectable in the Injector.
     * @param cls the class to register
     * @throws IllegalArgumentException if the class is not annotated with @Injectable
     */
    public static void register(Class<?> cls) {
        if (!cls.isAnnotationPresent(Injectable.class)) {
            throw new IllegalArgumentException("Class " + cls.getName() + " is not annotated with @Injectable");
        }

        Injectable.Scope scope = cls.getAnnotation(Injectable.class).scope();
        scopeRegistry.put(cls, scope);
    }

    /**
     * Binds an interface to an implementation
     * @param interfaceClass the interface class to bind
     * @param implClass the concrete implementation
     */
    public static void bind(Class<?> interfaceClass, Class<?> implClass){
        if (!interfaceClass.isInterface()){
            throw new ClassCreationException(interfaceClass.getName() + " is not an instance of " + implClass.getName());
        }

        if (!interfaceClass.isAssignableFrom(implClass)){
            throw new ClassCreationException("Class " + implClass.getName() + " is not an instance of " + interfaceClass.getName());
        }

        interfaceBindings.put(interfaceClass, implClass);
    }

    /**
     * Retrieves an instance of the specified class, injecting its dependencies as needed.
     * @param cls the class to retrieve an instance of
     * @return an instance of the specified class
     * @param <T> the type of the class
     * @throws IllegalArgumentException if the class is not registered
     * @throws ClassCreationException if the class instance cannot be created
     * @throws InjectionFailedException if a dependency cannot be injected
     */
    public static <T> T get(Class<T> cls) {
        return get(cls, null);
    }

    /**
     * Retrieves an instance of the specified class, injecting its dependencies as needed.
     * Allows overriding the scope for this retrieval.
     * @param cls the class to retrieve an instance of
     * @param overrideScope the scope to use for this retrieval, or null to use the registered scope
     * @return an instance of the specified class
     * @param <T> the type of the class
     * @throws IllegalArgumentException if the class is not registered
     * @throws ClassCreationException if the class instance cannot be created
     * @throws InjectionFailedException if a dependency cannot be injected
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls, Injectable.Scope overrideScope) {
        try {
            Class<?> implClass = resolveImplementation(cls);

            Injectable.Scope scope = overrideScope != null ? overrideScope : scopeRegistry.get(implClass);
            if (scope == null) throw new IllegalArgumentException("Class " + implClass.getName() + " is not registered");

            switch (scope) {
                case SINGLETON -> {
                    if (singletonRegistry.containsKey(implClass))
                        return (T) singletonRegistry.get(implClass);

                    T instance = (T) createInstance(implClass);
                    injectDependencies(instance);
                    singletonRegistry.put(implClass, instance);
                    return instance;
                }
                case FACTORY -> {
                    T instance = (T) createInstance(implClass);
                    injectDependencies(instance);
                    return instance;
                }
                default -> throw new IllegalArgumentException("Unknown scope: " + scope);
            }

        } catch (Exception e) {
            logger.error("Failed to create instance of {}: {}", cls.getName(), e.getMessage(), e);
            throw new ClassCreationException("Failed to create instance of: " + cls.getName() + e.getMessage());
        }
    }


    /**
     * Injects dependencies into the fields of the given instance that are annotated with @Inject.
     * @param instance the instance to inject dependencies into
     * @throws InjectionFailedException if a dependency cannot be injected
     */
    private static void injectDependencies(Object instance) {
        Class<?> cls = instance.getClass();

        while (cls != null) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Inject fieldAnnotation = field.getAnnotation(Inject.class);
                    Injectable.Scope fieldScope = fieldAnnotation.scope();

                    Object dependency = get(field.getType(), fieldScope);
                    try {
                        field.setAccessible(true);
                        field.set(instance, dependency);
                    } catch (Exception e) {
                        logger.error("Failed to inject dependency into field: {}{}", field.getName(), e.getMessage(), e);
                        throw new InjectionFailedException("Failed to inject dependency into field: " + field.getName());
                    }
                }
            }
            cls = cls.getSuperclass();
        }


    }

    /**
     * Resolves an interface to its implementation class
     * @param cls the class to resolve (maybe interface or concrete class)
     * @return the implementation class to use
     */
    private static <T> Class<?> resolveImplementation(Class<T> cls) {
        if (cls.isInterface()) {
            Class<?> impl = interfaceBindings.get(cls);
            if (impl == null)
                throw new ClassCreationException("No implementation found for interface: " + cls.getName());
            return impl;
        }
        return cls;
    }

    private static Object createInstance(Class<?> cls){
        Constructor<?>[] constructors = cls.getDeclaredConstructors();

        // sort constructors by number of parameters (ascending)
        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        List<String> errMessages = new  ArrayList<>();

        for (Constructor<?> constructor : constructors) {

            try {
                constructor.setAccessible(true);

                if (constructor.getParameterCount() == 0) {
                    return constructor.newInstance();
                }

                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = get(paramTypes[i]);
                }
                return constructor.newInstance(params);
            } catch (Exception e) {
                String errorMsg = String.format("Constructor with parameters %s failed: %s",
                        Arrays.toString(constructor.getParameterTypes()), e.getMessage());
                errMessages.add("Failed to access constructor: " + e.getMessage());
                logger.debug("Constructor attempt failed for {}: {}", cls.getName(), errorMsg);

            }

        }

        String errorMessage = String.format(
                "No suitable constructor found for %s. Attempted constructors:%n%s",
                cls.getName(),
                String.join("%n", errMessages)
        );

        logger.error(errorMessage);
        throw new ClassCreationException(errorMessage);

//        for (Constructor<?> constructor : constructors) {
//            if (constructor.getParameterCount() == 0) {
//                try {
//                    constructor.setAccessible(true);
//                    return constructor.newInstance();
//                } catch (Exception e) {
//                    throw new ClassCreationException("Failed to create instance of: " + cls.getName());
//                }
//            }
//        }
//
//        for (Constructor<?> constructor : constructors) {
//            try {
//                constructor.setAccessible(true);
//                Class<?>[] paramTypes = constructor.getParameterTypes();
//                Object[] params = new Object[paramTypes.length];
//                for (int i = 0; i < paramTypes.length; i++) {
//                    params[i] = get(paramTypes[i]);
//                }
//                return constructor.newInstance(params);
//            } catch (Exception e) {
//                // Continue to next constructor
//            }
//        }
    }
}
