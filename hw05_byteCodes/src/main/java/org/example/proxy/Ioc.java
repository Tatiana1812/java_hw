package org.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.example.annotations.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static TestLoggingInterface createMyClass(TestLoggingInterface clazz) {
        Set<String> annotatedMethods = findAnnotatedMethods(clazz.getClass());
        InvocationHandler handler = new TestLoggingInvocationHandler(clazz, annotatedMethods);
        return (TestLoggingInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }
    private static Set<String> findAnnotatedMethods(Class<?> clazz) {
        Set<String> result = new HashSet<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                result.add(getSignature(method));
            }
        }
        return result;
    }
    private static String getSignature(Method method) {
        StringBuilder sigBuilder = new StringBuilder(method.getName()).append('(');
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; ++i) {
            sigBuilder.append(paramTypes[i].getSimpleName());
            if (i < paramTypes.length - 1) {
                sigBuilder.append(", ");
            }
        }
        sigBuilder.append(')');
        return sigBuilder.toString();
    }

    static class TestLoggingInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final Set<String> annotatedMethods;

        TestLoggingInvocationHandler(TestLoggingInterface myClass, Set<String> annotatedMethods) {
            this.myClass = myClass;
            this.annotatedMethods = annotatedMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (annotatedMethods.contains(getSignature(method))) {
                logger.info("Executed method: {} with params: {}", methodName, args);
            }
            return method.invoke(myClass, args);
        }
    }
}

