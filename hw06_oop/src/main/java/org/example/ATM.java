package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class ATM {
    private static final Logger logger = LoggerFactory.getLogger(ATM.class);
    private final Map<Banknote, Integer> availableBanknotes;

    public ATM(Map<Banknote, Integer> initialBanknotes) {
        this.availableBanknotes = new EnumMap<>(initialBanknotes);
    }

    public void acceptBanknotes(Map<Banknote, Integer> deposits) {
        for (Map.Entry<Banknote, Integer> entry: deposits.entrySet()) {
            var banknote = entry.getKey();
            var count = entry.getValue();
            if (availableBanknotes.containsKey(banknote)) {
                availableBanknotes.merge(banknote, count, Integer::sum);
            } else {
                availableBanknotes.put(banknote, count);
            }
        }
    }
    public int getBalance() {
        int res = 0;
        for (Map.Entry<Banknote, Integer> entry: availableBanknotes.entrySet()) {
            var banknote = entry.getKey();
            var count = entry.getValue();
            res += banknote.getValue() * count;
        }
        return res;
    }
    public Map<Banknote, Integer>  withdraw(int amount) {
        Map<Banknote, Integer> originalState = new EnumMap<>(availableBanknotes);
        Map<Banknote, Integer> result = new LinkedHashMap<>();
        List<Banknote> sortedBanknotes = new ArrayList<>(availableBanknotes.keySet());
        sortedBanknotes.sort(Comparator.comparingInt(Banknote::getValue).reversed());

        for (Banknote banknote : sortedBanknotes) {
            int currentNominal = banknote.getValue();
            int availableCount = availableBanknotes.get(banknote);
            int count = Math.min(amount / currentNominal, availableCount);

            if (count > 0) {
                result.put(banknote, count);
                availableBanknotes.put(banknote, availableCount - count);
                amount -= count * currentNominal;
            }

            if (amount == 0) {
                break;
            }
        }
        if (amount != 0) {
            availableBanknotes.clear();
            availableBanknotes.putAll(originalState);
            throw new RuntimeException("Невозможно выдать указанную сумму доступными банкнотами.");
        }
        return result;
    }

    @Override
    public String toString() {
        return "ATM{" +
                "availableBanknotes=" + availableBanknotes +
                '}';
    }

    public Map<Banknote, Integer> getAvailableBanknotes() {
        return availableBanknotes;
    }
}

