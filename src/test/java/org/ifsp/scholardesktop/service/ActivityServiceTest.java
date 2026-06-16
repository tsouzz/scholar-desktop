package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IActivityDAO;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private IActivityDAO activityDAO;

    private ActivityService activityService;
    private Student student;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityDAO);
        student = new Student(1, "Aluno Teste", null);
    }

    @Test
    void createActivityShouldThrowWhenGradeIsNegative() {
        assertThrows(InvalidOperationException.class, () ->
                activityService.createActivity(ActivityType.AC_1, -1, student)
        );
    }

    @Test
    void createActivityShouldThrowWhenGradeExceedsTen() {
        assertThrows(InvalidOperationException.class, () ->
                activityService.createActivity(ActivityType.AC_1, 11, student)
        );
    }

    @Test
    void createActivityShouldThrowWhenActivityAlreadyExists() {
        Activity existing = new Activity(
                ActivityType.TO_MID, 8.0, LocalDate.now(), student
        );
        when(activityDAO.findByStudentAndType(student.getId(), ActivityType.TO_MID))
                .thenReturn(existing);

        assertThrows(InvalidOperationException.class, () ->
                activityService.createActivity(ActivityType.TO_MID, 9.0, student)
        );
    }

    @Test
    void createActivityShouldSucceedWithValidData() throws InvalidOperationException {
        when(activityDAO.findByStudentAndType(student.getId(), ActivityType.AC_1))
                .thenReturn(null);

        Activity result = activityService.createActivity(ActivityType.AC_1, 8.0, student);

        assertNotNull(result);
        assertEquals(ActivityType.AC_1, result.getActivityType());
        assertEquals(8.0, result.getGrade());
        verify(activityDAO, times(1)).insert(result);
    }
}