package com.ruoyi;


import java.math.BigDecimal;

/**
 * @author wsllt
 */
public class RuoYiApplicationTest {
    public static void main(String[] args) {
        BigDecimal loanAmount = BigDecimal.valueOf(500000L);
        Double rate = 0.049D;
        Integer loanMonth = 360;

//        BigDecimal monthRate = BigDecimal.valueOf(rate).divide(BigDecimal.valueOf(12), 8);
        BigDecimal monthRate = BigDecimal.valueOf(0.0049D);

        BigDecimal monthPay = loanAmount.multiply(monthRate)
                .multiply((monthRate.add(BigDecimal.ONE)).pow(loanMonth))
                .divide(((monthRate.add(BigDecimal.ONE)).pow(loanMonth)).subtract(BigDecimal.ONE), 6);

        System.out.println(monthPay);


    }
}
