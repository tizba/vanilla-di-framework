package fr.baptistemasoud.vanillaframework;

import fr.baptistemasoud.vanillaframework.annotation.ScannedComponent;
import fr.baptistemasoud.vanillaframework.core.ComponentInstantiator;
import fr.baptistemasoud.vanillaframework.core.ComponentScanner;
import fr.baptistemasoud.vanillaframework.exception.DependenciesCycleException;

import java.util.Set;

public class Main {
    public static void main(String[] args) throws DependenciesCycleException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide a package name to scan for components");
        }
        String packageName = args[0];

        Set<Class<?>> classes = ComponentScanner.scanAnnotatedClasses(packageName, ScannedComponent.class);

        ComponentInstantiator.instantiateClasses(classes);
    }
}
