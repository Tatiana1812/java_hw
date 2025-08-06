package org.example.proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation(6);
        myClass.calculation(6, 7);
        myClass.calculation(6, 7, "string");
    }
}
