package fr.baptistemasoud.vanillaframework.core;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    private ComponentScanner() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Scans for classes annotated with the specified annotation in the given package and its subpackages.
     *
     * @param packageName the name of the package to scan
     * @param annotation  the annotation to look for
     * @return a set of classes that are annotated with the specified annotation
     */
    public static Set<Class<?>> scanAnnotatedClasses(String packageName, Class<? extends Annotation> annotation) {
        Set<Class<?>> allClasses = getAllClasses(packageName);
        return filterAnnotatedClasses(allClasses, annotation);
    }

    /**
     * Retrieves all classes in the specified package and its subpackages.
     *
     * @param packageName the name of the package to scan
     * @return a set of all classes found in the package
     */
    private static Set<Class<?>> getAllClasses(String packageName) {
        File packageDirectory = getPackageDirectory(packageName);
        return findClassesInDirectory(packageDirectory, packageName);
    }

    /**
     * Recursively finds classes from the given directory and returns a set of them.
     *
     * @param directory   the directory to search
     * @param packageName the package name corresponding to the directory
     * @return a set of classes found in the directory
     */
    private static Set<Class<?>> findClassesInDirectory(File directory, String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(getClassFromFile(file, packageName));
                }
            }
        }
        return classes;
    }

    /**
     * Gets the directory for the specified package name.
     *
     * @param packageName the package name
     * @return the directory corresponding to the package
     */
    private static File getPackageDirectory(String packageName) {
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            throw new RuntimeException("No resources found for " + path);
        }
        return new File(resource.getFile());
    }

    /**
     * Loads a class from the specified file.
     *
     * @param file        the class file
     * @param packageName the package name of the class
     * @return the loaded class
     */
    private static Class<?> getClassFromFile(File file, String packageName) {
        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
    }

    /**
     * Filters the set of classes for those annotated with the specified annotation.
     *
     * @param classes    the set of classes to filter
     * @param annotation the annotation to filter by
     * @return a set of classes that are annotated with the specified annotation
     */
    private static Set<Class<?>> filterAnnotatedClasses(Set<Class<?>> classes, Class<? extends Annotation> annotation) {
        return classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }
}