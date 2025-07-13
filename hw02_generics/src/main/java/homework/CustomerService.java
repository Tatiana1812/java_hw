package homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private TreeMap<Customer, String> treeMap = new TreeMap<>(Comparator.comparingLong(customer -> customer.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = treeMap.firstEntry();
        Customer copyOfMin = new Customer(entry.getKey().getId(),
                entry.getKey().getName(),
                entry.getKey().getScores());
        return new AbstractMap.SimpleImmutableEntry<>(copyOfMin, entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry=  treeMap.higherEntry(customer);
        if (nextEntry == null) {
            return null;
        }

        Customer copyOfNext = new Customer(nextEntry.getKey().getId(),
                nextEntry.getKey().getName(),
                nextEntry.getKey().getScores());
        return new AbstractMap.SimpleImmutableEntry<>(copyOfNext, nextEntry.getValue());
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
