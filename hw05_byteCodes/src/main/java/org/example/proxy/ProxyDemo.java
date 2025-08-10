package org.example.proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass(new TestLoggingImpl());
        myClass.calculation(6);
        myClass.calculation(6, 7);
        myClass.calculation("one", "two");
        myClass.calculation(6, 7, "string");
    }
}
