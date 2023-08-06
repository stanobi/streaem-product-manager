package com.streaem.productmanager.util;

import java.math.BigDecimal;

public class Utils {

    private Utils() {
        /**
         * Empty Constructor
         */
    }

    public static BigDecimal getValue(BigDecimal value) {
        if (value == null) {
            return new BigDecimal("0.00");
        }
        return value;
    }

    public static String getValue(String value) {
        if (value == null || value.isEmpty()) {
            return "Unknown";
        }
        return value;
    }

}
