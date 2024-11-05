package fr.baptistemasoud.vanillaframework.core;

import fr.baptistemasoud.vanillaframework.exception.DependenciesCycleException;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

/**
 * The ComponentInstantiator class is responsible for instantiating
 * components from a given set of classes while managing their dependencies.
 */
public class ComponentInstantiator {

    private ComponentInstantiator() {
    }

    /**
     * Instantiates classes from the provided set of classes, ensuring that
     * all dependencies are resolved and that there are no cyclic dependencies.
     *
     * @param classes a set of classes to instantiate
     * @return a ComponentContainer containing the instantiated components
     * @throws DependenciesCycleException if a cyclic dependency is detected
     */
    public static ComponentContainer instantiateClasses(Set<Class<?>> classes) throws DependenciesCycleException {
        ComponentContainer container = new ComponentContainer();
        List<Class<?>> sortedClasses = TopologicalSorter.sort(classes);

        if (sortedClasses.size() != classes.size()) {
            for (Class<?> clazz : classes) {
                if (!sortedClasses.contains(clazz)) {
                    throw new DependenciesCycleException(clazz);
                }
            }
        }

        for (Class<?> clazz : sortedClasses) {
            instantiateClass(clazz, container);
        }

        return container;
    }

    /**
     * Instantiates a single class and adds it to the provided ComponentContainer.
     *
     * @param clazz     the class to instantiate
     * @param container the ComponentContainer to hold the instantiated component
     */
    private static void instantiateClass(Class<?> clazz, ComponentContainer container) {
        if (container.getInstance(clazz).isPresent()) {
            return; // Already instantiated
        }

        try {
            Object instance = createInstance(clazz, container);
            container.addInstance(clazz, instance);
        } catch (Exception e) {
            e.printStackTrace(); // Consider logging instead
        }
    }

    /**
     * Creates an instance of the specified class using its constructor,
     * resolving dependencies from the provided ComponentContainer.
     *
     * @param clazz     the class to create an instance of
     * @param container the ComponentContainer to resolve dependencies
     * @return a new instance of the specified class
     * @throws Exception if instantiation fails
     */
    private static Object createInstance(Class<?> clazz, ComponentContainer container) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = constructors[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                int finalI = i;
                parameters[i] = container.getInstance(parameterTypes[i])
                        .orElseThrow(() -> new IllegalStateException("No instance found for " + parameterTypes[finalI].getName()));
            }

            return constructor.newInstance(parameters);
        }
        throw new IllegalStateException("No suitable constructor found for " + clazz.getName());
    }
}