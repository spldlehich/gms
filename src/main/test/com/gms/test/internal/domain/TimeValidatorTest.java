package com.gms.test.internal.domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class TimeValidatorTest {

    /**
     * Актуальная задача
     */
    @Test
    public void testCheckRangeTrue() {
        LocalDateTime now = LocalDateTime.now();
        assertTrue(TimeValidator.checkRange(now));
    }

    /**
     * Неактуальная задача
     */
    @Test
    public void testCheckRangeFalse() {
        LocalDateTime now = LocalDateTime.now();
        now.plusDays(1);
        assertFalse(TimeValidator.checkRange(now));
    }

}
