package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Map<Banknote, Integer> initBanknotes = new EnumMap<>(Banknote.class){{
            put(Banknote.FIFTY, 10);
            put(Banknote.THOUSAND, 7);
            put(Banknote.FIVE_HUNDRED, 3);
        }};
        var atm = new ATM(initBanknotes);
        logger.info("balance after initialization : {}", atm.getBalance());
        Map<Banknote, Integer> newBanknotes = new EnumMap<>(Banknote.class){{
            put(Banknote.FIVE_THOUSAND, 2);
        }};
        atm.acceptBanknotes(newBanknotes);
        logger.info("balance after put money: {}", atm.getBalance());
        logger.info("get all banknotes: {}", atm.getAvailableBanknotes());
        logger.info("Планируется снятие 3250. Результат: {}",atm.withdraw(3250));
        logger.info("balance after withdraw: {}", atm.getBalance());
    }
}