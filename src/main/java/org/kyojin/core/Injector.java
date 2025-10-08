package org.kyojin.core;

import org.kyojin.core.exception.ClassCreationException;
import org.kyojin.core.exception.InjectionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Injector {
    private static final Map<Class<?>, Object> singletonRegistry = new HashMap<>();
    private static final Map<Class<?>, Injectable.Scope> scopeRegistry = new HashMap<>();
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

            if (folderPath == null) {
                throw new IllegalArgumentException("Package not found: " + packageName);
            }

            File dir = new File(folderPath.toURI());

            for (File file : dir.listFiles()) {
                // Skip core excluded packages
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
                }
            }


        } catch (Exception e) {
            logger.error("Failed to scan package: {}{}", packageName, e.getMessage(), e);
            throw new RuntimeException("Failed to scan package: " + packageName);
        }
    }

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
    public static <T> T get(Class<T> cls, Injectable.Scope overrideScope) {
        try {
            Injectable.Scope scope = overrideScope != null ? overrideScope : scopeRegistry.get(cls);

            if (scope == null) throw new IllegalArgumentException("Class " + cls.getName() + " is not registered");

            switch (scope) {
                case SINGLETON -> {
                    if (singletonRegistry.containsKey(cls)) {
                        return cls.cast(singletonRegistry.get(cls));
                    } else {
                        T instance = cls.getDeclaredConstructor().newInstance();
                        injectDependencies(instance);
                        singletonRegistry.put(cls, instance);
                        return instance;
                    }
                }
                case FACTORY -> {
                    T instance = cls.getDeclaredConstructor().newInstance();
                    injectDependencies(instance);
                    return instance;
                }
                default -> throw new IllegalArgumentException("Unknown scope: " + scope);
            }

        } catch (Exception e) {
            logger.error("Failed to create instance of class: {}{}", cls.getName(), e.getMessage(), e);
            throw new ClassCreationException("Failed to create instance of class: " + cls.getName());
        }
    }


    /**
     * Injects dependencies into the fields of the given instance that are annotated with @Inject.
     * @param instance the instance to inject dependencies into
     * @throws InjectionFailedException if a dependency cannot be injected
     */
    private static void injectDependencies(Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
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
    }

}
