package homework;

import java.util.*;

public class CustomerService {
    private final NavigableMap<Customer, String> treeMap = new TreeMap<>(Comparator.comparingLong(customer -> customer.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = treeMap.firstEntry();
        Customer copyOfMin = copyOfCustomer(entry);
        return new AbstractMap.SimpleImmutableEntry<>(copyOfMin, entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry=  treeMap.higherEntry(customer);
        if (nextEntry == null) {
            return null;
        }

        Customer copyOfNext = copyOfCustomer(nextEntry);
        return new AbstractMap.SimpleImmutableEntry<>(copyOfNext, nextEntry.getValue());
    }

    private Customer copyOfCustomer(Map.Entry<Customer, String> entry) {
        return new Customer(entry.getKey().getId(),
                entry.getKey().getName(),
                entry.getKey().getScores());
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
