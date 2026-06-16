package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IClassGroupDAO;
import org.ifsp.scholardesktop.dao.interfaces.IStudentDAO;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.*;
import org.ifsp.scholardesktop.model.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private IStudentDAO studentDAO;

    @Mock
    private IClassGroupDAO classGroupDAO;

    private StudentService studentService;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentDAO, classGroupDAO);
        teacher = new Teacher(1, "Thulio Souza", "thulio@email.com", "hash", new School(1, "CNA"));
    }

    @Test
    void transferStudentShouldSucceed_WhenSameModule() throws InvalidOperationException {
        ClassGroup origin = new ClassGroup(1, 1, Module.INTERMEDIATE_1, teacher);
        ClassGroup target = new ClassGroup(2, 2, Module.INTERMEDIATE_1, teacher);
        Student student = new Student(1, "Aluno Teste", origin);

        when(studentDAO.findById(1)).thenReturn(student);
        when(classGroupDAO.findById(2)).thenReturn(target);

        Student result = studentService.transferStudent(1, 2);

        assertEquals(target, result.getClassGroup());
        verify(studentDAO, times(1)).update(student);
    }

    @Test
    void transferStudentShouldThrow_WhenDifferentModule() {
        ClassGroup origin = new ClassGroup(1, 1, Module.INTERMEDIATE_1, teacher);
        ClassGroup target = new ClassGroup(2, 1, Module.ADVANCED_1, teacher);
        Student student = new Student(1, "Aluno Teste", origin);

        when(studentDAO.findById(1)).thenReturn(student);
        when(classGroupDAO.findById(2)).thenReturn(target);

        assertThrows(InvalidOperationException.class, () ->
                studentService.transferStudent(1, 2)
        );

        verify(studentDAO, never()).update(any());
    }
}