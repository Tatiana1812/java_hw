package org.example;

import org.example.annotations.After;
import org.example.annotations.Before;
import org.example.annotations.Test;
class LifeCycleTest {

    // Подготовительные мероприятия. Метод выполнится перед каждым тестом
    @Before
    public void setUp() {
        System.out.println("\n@BeforeEach. ");
        System.out.printf("Экземпляр тестового класса: %s%n", Integer.toHexString(hashCode()));
    }

    // Сам тест
    @Test
    void anyTest1() {
        System.out.println("@Test: anyTest1. ");
        System.out.printf("Экземпляр тестового класса: %s%n", Integer.toHexString(hashCode()));
    }

    // Еще тест
    @Test
    void anyTest2() {
        System.out.println("@Test: anyTest2. ");
        System.out.printf("Экземпляр тестового класса: %s%n", Integer.toHexString(hashCode()));
    }

    // Еще тест
    @Test
    void anyTest3() {
        System.out.println("@Test: anyTest2. ");
        System.out.printf("Экземпляр тестового класса: %s%n", Integer.toHexString(hashCode()));
        throw new RuntimeException("Тест 4 не прошел");
    }

    // Еще тест
    @Test
    void anyTest4() {
        System.out.println("@Test: anyTest4. ");
        System.out.printf("Экземпляр тестового класса: %s%n", Integer.toHexString(hashCode()));
    }

    // Завершающие мероприятия. Метод выполнится после каждого теста
    @After
    public void tearDown() {
        System.out.println("@AfterEach. ");
        System.out.printf("Экземпляр тестового класса: %s%n", Integer.toHexString(hashCode()));
    }

}
