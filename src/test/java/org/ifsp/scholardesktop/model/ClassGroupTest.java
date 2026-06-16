package org.ifsp.scholardesktop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClassGroupTest {

    @Test
    void getNameShouldReturnCorrectFormat() {
        ClassGroup classGroup = new ClassGroup(1, Module.PRE_ADVANCED_1, null);
        assertEquals("PADV1-0001", classGroup.getName());
    }

    @Test
    void getNameShouldPadNumberWithZeros() {
        ClassGroup classGroup = new ClassGroup(42, Module.INTERMEDIATE_1, null);
        assertEquals("INT1-0042", classGroup.getName());
    }

    @Test
    void getNameShouldHandleMaxNumber() {
        ClassGroup classGroup = new ClassGroup(9999, Module.MASTER_1, null);
        assertEquals("MAST1-9999", classGroup.getName());
    }

    @Test
    void getNameShouldReflectModuleChange() {
        ClassGroup classGroup = new ClassGroup(1, Module.BASIC_1, null);
        assertEquals("BAS1-0001", classGroup.getName());

        classGroup.setModule(Module.ADVANCED_1);
        assertEquals("ADV1-0001", classGroup.getName());
    }
}