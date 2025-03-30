package ru.otus.pro.reflection.processors;

import ru.otus.pro.reflection.annotations.*;
import ru.otus.pro.reflection.exceptions.AnnotationUsageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestsCollector implements Collector {
    private final static int PRIORITY_MIN = 1;
    private final static int PRIORITY_MAX = 10;

    private final List<? extends Class<?>> testClasses;

    public TestsCollector(String packageName) {
        testClasses = findAllTestClassesUsingClassLoader(packageName);
    }

    public Map<Class<?>, List<Wrapper>> collect() {
        return testClasses.stream().collect(Collectors.toMap(c -> c, this::getMethods));
    }

    private Method getSuiteMethod(List<Method> allMethods, Class<? extends Annotation> hookAnnotation) {
        var suiteMethods = allMethods.stream().filter(c ->
                c.isAnnotationPresent(hookAnnotation)).toList();

        if (suiteMethods.size() > 1) {
            throw new AnnotationUsageException(String.format("%s может быть использован только один раз в классе.", hookAnnotation.getSimpleName()));
        }
        var suiteMethod = suiteMethods.getFirst();
        if (suiteMethod.isAnnotationPresent(BeforeSuite.class) && suiteMethod.isAnnotationPresent(AfterSuite.class)) {
            throw new AnnotationUsageException(String.format("%s и %s не могут быть использованы вместе для одного и того же метода.", BeforeSuite.class.getSimpleName(), AfterSuite.class.getSimpleName()));
        }
        wrongCombinationValidation(suiteMethod, hookAnnotation, Test.class);
        wrongCombinationValidation(suiteMethod, hookAnnotation, Disabled.class);
        return suiteMethod;
    }

    private void wrongCombinationValidation(Method method, Class<? extends Annotation> prezentAnnotation, Class<? extends Annotation> notPrezentAnnotation) {
        if (method.isAnnotationPresent(notPrezentAnnotation)) {
            throw new AnnotationUsageException(String.format("%s не может быть использован вместе с %s.", prezentAnnotation.getSimpleName(), notPrezentAnnotation.getSimpleName()));
        }
    }

    private void combinationValidation(Method method, Class<? extends Annotation> presentAnnotation, Class<? extends Annotation> requiredAnnotation) {
        if (!method.isAnnotationPresent(requiredAnnotation)) {
            throw new AnnotationUsageException(String.format("%s может быть использован только вместе с %s.", presentAnnotation.getSimpleName(), requiredAnnotation.getSimpleName()));
        }
    }

    private void priorityValidation(int priority) {
        if (priority < PRIORITY_MIN || priority > PRIORITY_MAX) {
            throw new AnnotationUsageException(String.format("Приоритет тестов может быть установлен в диапазоне от %d до %d.", PRIORITY_MIN, PRIORITY_MAX));
        }
    }


    private List<Wrapper> getMethods(Class<?> clazz) {
        var allMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(c ->
                        c.isAnnotationPresent(BeforeSuite.class) ||
                                c.isAnnotationPresent(AfterSuite.class) ||
                                c.isAnnotationPresent(Test.class) ||
                                c.isAnnotationPresent(Disabled.class))
                .toList();

        var before = getSuiteMethod(allMethods, BeforeSuite.class);

        var after = getSuiteMethod(allMethods, AfterSuite.class);

        allMethods.stream().filter(m -> m.isAnnotationPresent(Disabled.class)).forEach(m -> combinationValidation(m, Disabled.class, Test.class));
        var methods = allMethods.stream().filter(m -> m.isAnnotationPresent(Test.class)).map(m -> new MethodWrapper(clazz, m)).collect(Collectors.toList());
        for (var methodWrapper : methods) {
            priorityValidation(methodWrapper.getPriority());
        }
        if (before != null) {
            methods.add(new MethodWrapper(clazz, before));
        }
        if (after != null) {
            methods.add(new MethodWrapper(clazz, after));
        }

        return methods.stream().sorted().map(m -> (Wrapper) m).toList();
    }


    private List<? extends Class<?>> findAllTestClassesUsingClassLoader(String packageName) {
        try (InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> getClass(line, packageName))
                    .filter(Objects::nonNull)
                    .filter(c -> c.isAnnotationPresent(TestContext.class))
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
