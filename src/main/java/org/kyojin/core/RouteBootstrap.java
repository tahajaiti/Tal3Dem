package org.kyojin.core;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.kyojin.core.annotation.Route;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class RouteBootstrap {

    private static final Logger logger = Logger.getLogger(RouteBootstrap.class.getName());

    public static void register(ServletContext context, String packageName) throws ClassNotFoundException {
        List<Class<?>> classList = findClasses(packageName);

        for (Class<?> cls : classList) {
            if (cls.isAnnotationPresent(Route.class)) {
                Route route = cls.getAnnotation(Route.class);
                String path = route.value();
                context.addServlet(cls.getSimpleName(), (Class<? extends Servlet>) cls).addMapping(path);
                logger.info("Registered route: " + path + " -> " + cls.getName());
            }
        }

    }

    private static List<Class<?>> findClasses(String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL folderPath = Thread.currentThread().getContextClassLoader().getResource(path);
        if (folderPath == null) throw new IllegalArgumentException("Package not found: " + packageName);

        File dir = new File(folderPath.getFile());
        if (!dir.exists()) throw new IllegalArgumentException("Directory not found: " + dir.getAbsolutePath());

        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()){
                classes.addAll(findClasses(packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }


}
