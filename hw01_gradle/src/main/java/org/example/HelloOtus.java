package org.example;

import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("яблоко", "груша", "банан");
        String result = Joiner.on(", ").join(fruits);
        System.out.println(result);
    }
}
