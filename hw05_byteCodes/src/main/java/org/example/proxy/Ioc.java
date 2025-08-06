package org.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.example.annotations.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new TestLoggingInvocationHandler(new TestLoggingImpl());
        return (TestLoggingInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class TestLoggingInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;

        TestLoggingInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean hasAnnotated = false;
            try {
                Method realMethod  = myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
                hasAnnotated = realMethod.isAnnotationPresent(Log.class);
            } catch (NoSuchMethodException e) {
                logger.error("No such method");
            }
            if (hasAnnotated) {
                logger.info("executed method: {}, param: {}", method, args);
            }
            return method.invoke(myClass, args);
        }
    }
}

