package homework;

import java.util.ArrayDeque;

public class CustomerReverseOrder {

    ArrayDeque<Customer> deque = new ArrayDeque<>();

    public void add(Customer customer) {
        deque.addLast(customer);
    }

    public Customer take() {
        return deque.pollLast();
    }
}
