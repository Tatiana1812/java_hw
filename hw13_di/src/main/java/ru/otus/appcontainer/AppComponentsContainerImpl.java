package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.DuplicateComponentException;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configInstance;
        try {
            configInstance = configClass.getConstructor().newInstance();

            var methods = configClass.getDeclaredMethods();
            Arrays.sort(methods, Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()));

            for (var method : methods) {
                if (method.isAnnotationPresent(AppComponent.class)) {
                    createAndRegisterComponent(configInstance, method);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Ошибка инициализации контейнера", e);
        }
    }
    private void createAndRegisterComponent(Object configInstance, Method method) throws InvocationTargetException, IllegalAccessException {
        AppComponent annotation = method.getAnnotation(AppComponent.class);
        String componentName = annotation.name();

        if (appComponentsByName.containsKey(componentName)) {
            throw new DuplicateComponentException("Компонент с таким именем (" + componentName + ") уже существует");
        }

        var dependencies = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toList()
                .toArray(new Object[0]);

        Object component = method.invoke(configInstance, dependencies);

        registerComponent(componentName, component);
    }
    private void registerComponent(String componentName, Object component) {
        appComponentsByName.put(componentName, component);
        appComponents.add(component);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components =
                appComponents.stream().filter(componentClass::isInstance).toList();
        if (components.size() == 1) {
            return (C) components.getFirst();
        }
        throw new NoSuchElementException("Компонент отсутствует или дублируется: " + componentClass.getSimpleName());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component != null) {
            return (C) component;
        }
        throw new NoSuchElementException("No such component found by name: " + componentName);
    }
}
