package org.example;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class App5 {

    public static BigDecimal roundMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    @Data
    @Builder
    @ToString
    public static class Bond {
        @NonNull
        private String name;
        @NonNull
        private String isin;
        @NonNull
        private Double nominal;
        @NonNull
        private Double nkd;

        private Double coupon;
        /** переодичность выплаты купона в днях*/
        private Integer couponPeriodicity;

        /** цена продажи/погашения (обычно - номинал)*/
        private Double salePrice;

        private LocalDate saleDate;
    }

    @Data
    @Builder
    public static class Order {
        @NonNull
        private Bond bond;
        private Double quantity;
        /**
         * дата покупки
         */
        private LocalDate orderDate;

        /**
         * рыночная цена в % от номинала
         */
        private Double marketPrice;

        /**
         * фиксированная ставка коммиссии при покупке
         */
        private Double commissionRate;
        /**
         * Дата выплаты первого купона
         */
        private LocalDate firstCouponPayment;

        /**
         * сумма покупки
         */
        public Double amount() {
            BigDecimal nominal = BigDecimal.valueOf(getBond().getNominal());
            BigDecimal marketPrice = BigDecimal.valueOf(getMarketPrice() / 100.0);
            BigDecimal price = roundMoney(nominal.multiply(marketPrice));
            return roundMoney(BigDecimal.valueOf(getQuantity()).multiply(price)).doubleValue();
        }
    }

    public static void main(String[] args) {
        Bond bond = Bond.builder()
            .name("Трансфин в4")
            .nominal(1000.0)
            .isin("XS2116222451")
            .coupon(4.3)
            .build();

        Order order = Order.builder()
            .bond(bond)
            .build();

        System.out.println(bond);
    }
}
