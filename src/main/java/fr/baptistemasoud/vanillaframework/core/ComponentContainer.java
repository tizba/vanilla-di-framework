package fr.baptistemasoud.vanillaframework.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A container for instances of components.
 */
public class ComponentContainer {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    /**
     * Adds an instance to the container.
     *
     * @param clazz    the class of the instance
     * @param instance the instance to add
     */
    public void addInstance(Class<?> clazz, Object instance) {
        instances.put(clazz, instance);
    }

    /**
     * Returns an instance of the provided class if it exists in the container.
     *
     * @param clazz the class to retrieve an instance of
     * @return an optional containing the instance if present in the container, empty otherwise
     */
    public Optional<Object> getInstance(Class<?> clazz) {
        return Optional.ofNullable(instances.get(clazz));
    }

    /**
     * Returns all instances in the container.
     *
     * @return a collection of instances
     */
    public Collection<Object> getInstances() {
        return instances.values();
    }
}
