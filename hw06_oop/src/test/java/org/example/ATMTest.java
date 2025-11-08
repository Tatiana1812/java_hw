package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ATMTest {
    private static final Logger logger = LoggerFactory.getLogger(ATMTest.class);

    @Test
    @DisplayName("Проверяем, что приём банкнот работает успешно ")
    void acceptBanknotesTest() {
        // given
        Map<Banknote, Integer> initBanknotes = new HashMap<>(){{
            put(Banknote.FIFTY, 10);
            put(Banknote.THOUSAND, 7);
            put(Banknote.FIVE_HUNDRED, 3);
        }};
        var atm = new ATM(initBanknotes);
        var oldBalance = atm.getBalance();

        Map<Banknote, Integer> newBanknotes = new HashMap<>(){{
            put(Banknote.FIVE_THOUSAND, 2);
        }};

        // when
        atm.acceptBanknotes(newBanknotes);

        // then
        assertThat(atm.getBalance()).isEqualTo(oldBalance + getMoney(newBanknotes));
    }
    @Test
    @DisplayName("Проверяем снятие средств, больше баланса")
    void withdrawErrorTest() {
        // given
        Map<Banknote, Integer> initBanknotes = new HashMap<>(){{
            put(Banknote.FIFTY, 2);
            put(Banknote.THOUSAND, 3);
            put(Banknote.FIVE_HUNDRED, 3);
        }};
        var atm = new ATM(initBanknotes);
        var oldBalance = atm.getBalance();
        var withdraw = 5700;
        logger.info("Хотим снять {} c текущего баланса {}", withdraw, oldBalance);

        //when & then
        Assertions.assertThatThrownBy(() -> atm.withdraw(withdraw))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Невозможно выдать указанную сумму доступными банкнотами");
    }
    @Test
    @DisplayName("Проверяем снятие средств, которые невозможно снять")
    void withdrawNotEnoughtMoneyTest() {
        // given
        Map<Banknote, Integer> initBanknotes = new HashMap<>(){{
            put(Banknote.FIFTY, 2);
            put(Banknote.THOUSAND, 3);
            put(Banknote.FIVE_HUNDRED, 3);
        }};
        var atm = new ATM(initBanknotes);
        var oldBalance = atm.getBalance();
        var withdraw = 150;
        logger.info("Хотим снять {} c текущего баланса {}", withdraw, oldBalance);

        //when & then
        Assertions.assertThatThrownBy(() -> atm.withdraw(withdraw))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Невозможно выдать указанную сумму доступными банкнотами");
    }
    @Test
    @DisplayName("Проверяем снятие средств")
    void withdrawSuccessTest() {
        // given
        Map<Banknote, Integer> initBanknotes = new EnumMap<>(Banknote.class){{
            put(Banknote.FIFTY, 2);
            put(Banknote.THOUSAND, 3);
            put(Banknote.FIVE_HUNDRED, 3);
        }};
        var atm = new ATM(initBanknotes);
        var oldBalance = atm.getBalance();
        var withdraw = 1550;
        logger.info("Хотим снять {} c текущего баланса {}", withdraw, oldBalance);

        // when
        var res = atm.withdraw(withdraw);

        // then
        assertThat(res).isEqualTo(new EnumMap<>(Banknote.class){{
            put(Banknote.FIFTY, 1);
            put(Banknote.THOUSAND, 1);
            put(Banknote.FIVE_HUNDRED, 1);
        }});
    }
    @Test
    @DisplayName("Проверяем снятие и приём средств")
    void withdrawAndAcceptSuccessTest() {
        // given
        Map<Banknote, Integer> initBanknotes = new EnumMap<>(Banknote.class){{
            put(Banknote.FIFTY, 10);
            put(Banknote.THOUSAND, 4);
            put(Banknote.FIVE_HUNDRED, 1);
        }};
        var atm = new ATM(initBanknotes);
        var oldBalance = atm.getBalance();
        var withdraw = 5550;

        // when & then
        Assertions.assertThatThrownBy(() -> atm.withdraw(withdraw))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Невозможно выдать указанную сумму доступными банкнотами");

        Map<Banknote, Integer> newBanknotes = new EnumMap<>(Banknote.class){{
            put(Banknote.FIVE_THOUSAND, 2);
        }};

        // when
        atm.acceptBanknotes(newBanknotes);

        // then
        assertThat(atm.getBalance()).isEqualTo(oldBalance + getMoney(newBanknotes));

        // when
        var res = atm.withdraw(withdraw);

        // then
        assertThat(res).isEqualTo(new HashMap<>(){{
            put(Banknote.FIFTY, 1);
            put(Banknote.FIVE_HUNDRED, 1);
            put(Banknote.FIVE_THOUSAND, 1);
        }});
    }
    private int getMoney(Map<Banknote, Integer> banknotes) {
        int res = 0;
        for (Map.Entry<Banknote, Integer> entry: banknotes.entrySet()) {
            var banknote = entry.getKey();
            var count = entry.getValue();
            res += banknote.getValue() * count;
        }
        return res;
    }
}
