package fr.baptistemasoud.vanillaframework.exception;

public class DependenciesCycleException extends Exception {
    public DependenciesCycleException(Class<?> clazz) {
        super("Cycle detected in dependencies of " + clazz.getName());
    }
}
