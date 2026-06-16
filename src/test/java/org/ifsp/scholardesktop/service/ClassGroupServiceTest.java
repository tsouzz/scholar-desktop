package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IClassGroupDAO;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.model.School;
import org.ifsp.scholardesktop.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassGroupServiceTest {

    @Mock
    private IClassGroupDAO classGroupDAO;

    private ClassGroupService classGroupService;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        classGroupService = new ClassGroupService(classGroupDAO);
        teacher = new Teacher(1, "Thulio Souza", "thulio@email.com", "hash", new School(1, "CNA"));
    }

    @Test
    void createClassGroupShouldAssignNumberOne_WhenFirstOfModule() {
        when(classGroupDAO.countByModuleAndTeacher(Module.INTERMEDIATE_1, teacher.getId()))
                .thenReturn(0);

        ClassGroup result = classGroupService.createClassGroup(Module.INTERMEDIATE_1, teacher);

        assertEquals(1, result.getNumber());
        assertEquals(Module.INTERMEDIATE_1, result.getModule());
        verify(classGroupDAO, times(1)).insert(result);
    }

    @Test
    void createClassGroupShouldAssignNumberTwo_WhenSecondOfModule() {
        when(classGroupDAO.countByModuleAndTeacher(Module.INTERMEDIATE_1, teacher.getId()))
                .thenReturn(1);

        ClassGroup result = classGroupService.createClassGroup(Module.INTERMEDIATE_1, teacher);

        assertEquals(2, result.getNumber());
        verify(classGroupDAO, times(1)).insert(result);
    }

    @Test
    void createClassGroupShouldHaveIndependentNumbers_ForDifferentModules() {
        when(classGroupDAO.countByModuleAndTeacher(Module.BASIC_1, teacher.getId()))
                .thenReturn(2);
        when(classGroupDAO.countByModuleAndTeacher(Module.ADVANCED_1, teacher.getId()))
                .thenReturn(0);

        ClassGroup basic = classGroupService.createClassGroup(Module.BASIC_1, teacher);
        ClassGroup advanced = classGroupService.createClassGroup(Module.ADVANCED_1, teacher);

        assertEquals(3, basic.getNumber());
        assertEquals(1, advanced.getNumber());
    }
}