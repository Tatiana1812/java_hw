package org.example;

import org.example.annotations.After;
import org.example.annotations.Before;
import org.example.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class TestRunner {

    /**
     * Запускает тестирование указанного класса.
     *
     * @param clazz Имя класса с тестами.
     */

    public static <T> void run(Class<T> clazz) {
        Map<Class<? extends Annotation>, List<Method>> annotatedMethodsMap = collectAnnotatedMethods(clazz);

        List<TestResult> results = new ArrayList<>();

        try {
            List<Method> testMethods = annotatedMethodsMap.get(Test.class);

            for (Method testMethod : testMethods) {
                results.add(runSingleTest(clazz, testMethod, annotatedMethodsMap));
            }
        } catch (Throwable e) {
            System.err.println("Ошибка при выполнении тестов: " + e.getMessage());
        }

        printStatistics(results);
    }


    /**
     * Собирает все методы класса по указанным аннотациям.
     *
     * @param clazz класс с тестами
     * @return карта аннотаций и соответствующих методов
     */
    private static Map<Class<? extends Annotation>, List<Method>> collectAnnotatedMethods(Class<?> clazz) {
        Map<Class<? extends Annotation>, List<Method>> map = new HashMap<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Before.class)) {
                map.computeIfAbsent(Before.class, k -> new ArrayList<>()).add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                map.computeIfAbsent(Test.class, k -> new ArrayList<>()).add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                map.computeIfAbsent(After.class, k -> new ArrayList<>()).add(method);
            }
        }

        return map;
    }
    /**
     * Выполнение одного теста.
     *
     * @param clazz Класс с тестами.
     * @param testMethod Метод, помеченный аннотацией @Test.
     * @return Результат выполнения теста.
     */
    private static <T> TestResult runSingleTest(
            Class<T> clazz,
            Method testMethod,
            Map<Class<? extends Annotation>, List<Method>> annotatedMethodsMap
    ) {
        Object instance = null;
        Throwable error = null;

        try {
            instance = instantiate(clazz);
            executeAnnotatedMethods(instance, Before.class);
            testMethod.invoke(instance);
        } catch ( IllegalAccessException |
                 InvocationTargetException ex) {
            error = ex.getCause() != null ? ex.getCause() : ex;
        } finally {
            if (instance != null) {
                executeAnnotatedMethods(instance, After.class);
            }
        }

        return new TestResult(testMethod.getName(), error);
    }
    /**
     * Создает объект указанного класса
     *
     * @param type экземпляр теста
     * @param args параметры теста
     */
    private static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
    /**
     * Выполняет все методы с указанной аннотацией для заданного экземпляра класса.
     *
     * @param instance Экземпляр класса.
     * @param annotationClass Тип аннотации (@Before/@After).
     */
    private static void executeAnnotatedMethods(Object instance, Class<? extends Annotation> annotationClass) {
        Method[] methods = instance.getClass().getDeclaredMethods();

        for (Method m : methods) {
            if (m.isAnnotationPresent(annotationClass)) {
                try {
                    m.setAccessible(true);
                    m.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.println("Ошибка при выполнении метода: " + m.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Печать статистики выполнения всех тестов.
     *
     * @param results Список результатов тестов.
     */
    private static void printStatistics(List<TestResult> results) {
        int totalTests = results.size();
        int passedTests = 0;
        int failedTests = 0;

        for (TestResult result : results) {
            if (result.error == null) {
                passedTests++;
            } else {
                failedTests++;
            }
        }

        System.out.println("\nТестирование завершилось:");
        System.out.println("Всего тестов: " + totalTests);
        System.out.println("Успешно пройдено: " + passedTests);
        System.out.println("Провалено: " + failedTests);
    }

    /**
     * Хранит результат выполнения отдельного теста.
     */
    private static class TestResult {
        private String name;
        private Throwable error;

        TestResult(String name, Throwable error) {
            this.name = name;
            this.error = error;
        }
    }

    private TestRunner() {}
}