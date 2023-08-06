package com.streaem.productmanager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void given_notNullValue_when_getValue_should_returnSameValue() {
        Assertions.assertEquals("Test", Utils.getValue("Test"));
        Assertions.assertEquals(BigDecimal.ONE, Utils.getValue(BigDecimal.ONE));
    }

    @Test
    void given_nullOrEmptyValue_when_getValue_should_returnDefaultValue() {
        Assertions.assertEquals("Unknown", Utils.getValue(""));
        Assertions.assertEquals("Unknown", Utils.getValue((String)null));
        Assertions.assertEquals(new BigDecimal("0.00"), Utils.getValue((BigDecimal)null));
    }
}