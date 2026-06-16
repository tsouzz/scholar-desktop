package org.ifsp.scholardesktop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTypeTest {

    @Test
    void weightsShouldSumToOne() {
        double total = 0.0;
        for (ActivityType type : ActivityType.values()) {
            total += type.getWeight();
        }
        assertEquals(1.0, total, 0.0001);
    }

    @Test
    void eachTypeShouldHavePositiveWeight() {
        for (ActivityType type : ActivityType.values()) {
            assertTrue(type.getWeight() > 0,
                    "Peso inválido para: " + type.name());
        }
    }

    @Test
    void eachTypeShouldHaveLabel() {
        for (ActivityType type : ActivityType.values()) {
            assertNotNull(type.getLabel());
            assertFalse(type.getLabel().isEmpty(),
                    "Label vazio para: " + type.name());
        }
    }
}