package fr.baptistemasoud.vanillaframework.core;

import java.lang.reflect.Constructor;
import java.util.*;

public class TopologicalSorter {
    private TopologicalSorter() {
    }

    /**
     * Performs a topological sort on a set of classes based on their constructor dependencies.
     *
     * @param classes A set of classes to be sorted.
     * @return A list of classes sorted in topological order.
     */
    public static List<Class<?>> sort(Set<Class<?>> classes) {
        Map<Class<?>, Set<Class<?>>> dependencyGraph = buildDependencyGraph(classes);
        Map<Class<?>, Integer> inDegree = calculateInDegrees(classes, dependencyGraph);
        return performTopologicalSort(classes, dependencyGraph, inDegree);
    }

    private static Map<Class<?>, Set<Class<?>>> buildDependencyGraph(Set<Class<?>> classes) {
        Map<Class<?>, Set<Class<?>>> dependencyGraph = new HashMap<>();
        for (Class<?> clazz : classes) {
            dependencyGraph.put(clazz, new HashSet<>());
        }
        return dependencyGraph;
    }

    private static Map<Class<?>, Integer> calculateInDegrees(Set<Class<?>> classes,
                                                             Map<Class<?>, Set<Class<?>>> dependencyGraph) {
        Map<Class<?>, Integer> inDegree = new HashMap<>();
        for (Class<?> clazz : classes) {
            inDegree.put(clazz, 0);
        }

        for (Class<?> clazz : classes) {
            addDependencies(clazz, dependencyGraph, inDegree, classes);
        }
        return inDegree;
    }

    private static void addDependencies(Class<?> clazz,
                                        Map<Class<?>, Set<Class<?>>> dependencyGraph,
                                        Map<Class<?>, Integer> inDegree,
                                        Set<Class<?>> classes) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length > 0) {
            Class<?>[] parameterTypes = constructors[0].getParameterTypes();
            for (Class<?> paramType : parameterTypes) {
                if (classes.contains(paramType)) {
                    dependencyGraph.get(paramType).add(clazz);
                    inDegree.put(clazz, inDegree.get(clazz) + 1);
                }
            }
        }
    }

    private static List<Class<?>> performTopologicalSort(Set<Class<?>> classes,
                                                         Map<Class<?>, Set<Class<?>>> dependencyGraph,
                                                         Map<Class<?>, Integer> inDegree) {
        Queue<Class<?>> zeroInDegreeQueue = new LinkedList<>();
        initializeZeroInDegreeQueue(classes, inDegree, zeroInDegreeQueue);

        List<Class<?>> sortedClasses = new ArrayList<>();
        while (!zeroInDegreeQueue.isEmpty()) {
            Class<?> currentClass = zeroInDegreeQueue.poll();
            sortedClasses.add(currentClass);
            reduceInDegrees(currentClass, dependencyGraph, inDegree, zeroInDegreeQueue);
        }

        return sortedClasses;
    }

    private static void initializeZeroInDegreeQueue(Set<Class<?>> classes,
                                                    Map<Class<?>, Integer> inDegree,
                                                    Queue<Class<?>> zeroInDegreeQueue) {
        for (Class<?> clazz : classes) {
            if (inDegree.get(clazz) == 0) {
                zeroInDegreeQueue.add(clazz);
            }
        }
    }

    private static void reduceInDegrees(Class<?> currentClass,
                                        Map<Class<?>, Set<Class<?>>> dependencyGraph,
                                        Map<Class<?>, Integer> inDegree,
                                        Queue<Class<?>> zeroInDegreeQueue) {
        for (Class<?> dependentClass : dependencyGraph.get(currentClass)) {
            inDegree.put(dependentClass, inDegree.get(dependentClass) - 1);
            if (inDegree.get(dependentClass) == 0) {
                zeroInDegreeQueue.add(dependentClass);
            }
        }
    }
}