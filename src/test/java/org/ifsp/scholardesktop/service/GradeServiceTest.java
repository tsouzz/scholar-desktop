package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.model.Activity;
import org.ifsp.scholardesktop.model.ActivityType;
import org.ifsp.scholardesktop.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradeServiceTest {

    private GradeService gradeService;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        gradeService = new GradeService();
        mockStudent = new Student("Aluno", null);
    }

    private Activity activity(ActivityType type, double grade) {
        return new Activity(type, grade, LocalDate.now(), mockStudent);
    }

    @Test
    void calculateShouldReturnZero() {
        double result = gradeService.calculateGrade(Collections.emptyList());
        assertEquals(0.0, result);
    }

    @Test
    void calculateShouldReturnMax() {
        List<Activity> activities = List.of(
                activity(ActivityType.AC_1,      10),
                activity(ActivityType.AC_2,      10),
                activity(ActivityType.LC_1,      10),
                activity(ActivityType.LC_2,      10),
                activity(ActivityType.LC_3,      10),
                activity(ActivityType.LC_4,      10),
                activity(ActivityType.LC_5,      10),
                activity(ActivityType.LC_6,      10),
                activity(ActivityType.LC_7,      10),
                activity(ActivityType.LC_8,      10),
                activity(ActivityType.LW_1,      10),
                activity(ActivityType.LW_2,      10),
                activity(ActivityType.LW_3,      10),
                activity(ActivityType.LW_4,      10),
                activity(ActivityType.LW_5,      10),
                activity(ActivityType.LW_6,      10),
                activity(ActivityType.LW_7,      10),
                activity(ActivityType.LW_8,      10),
                activity(ActivityType.TO_MID,    10),
                activity(ActivityType.TO_FINAL,  10),
                activity(ActivityType.TE_MID,    10),
                activity(ActivityType.TE_FINAL,  10)
        );

        double result = gradeService.calculateGrade(activities);
        assertEquals(100.0, result, 0.01);
    }

    @Test
    void calculateShouldReturnPartial() {
        List<Activity> activities = List.of(
                activity(ActivityType.TO_MID, 10)
        );

        double result = gradeService.calculateGrade(activities);
        assertEquals(10.0, result, 0.01);
    }

    @Test
    void calculateMaxShouldReturnZero(){
        double result = gradeService.calculateGrade(Collections.emptyList());
        assertEquals(0.0, result);
    }

    @Test
    void calculateMaxShouldReturnMax(){
        List<Activity> activities = List.of(
                activity(ActivityType.AC_1,      0),
                activity(ActivityType.AC_2,      0),
                activity(ActivityType.LC_1,      0),
                activity(ActivityType.LC_2,      0),
                activity(ActivityType.LC_3,      0),
                activity(ActivityType.LC_4,      0),
                activity(ActivityType.LC_5,      0),
                activity(ActivityType.LC_6,      0),
                activity(ActivityType.LC_7,      0),
                activity(ActivityType.LC_8,      0),
                activity(ActivityType.LW_1,      0),
                activity(ActivityType.LW_2,      0),
                activity(ActivityType.LW_3,      0),
                activity(ActivityType.LW_4,      0),
                activity(ActivityType.LW_5,      0),
                activity(ActivityType.LW_6,      0),
                activity(ActivityType.LW_7,      0),
                activity(ActivityType.LW_8,      0),
                activity(ActivityType.TO_MID,    0),
                activity(ActivityType.TO_FINAL,  0),
                activity(ActivityType.TE_MID,    0),
                activity(ActivityType.TE_FINAL,  0)
        );

        double result = gradeService.calculateMaxPossible(activities);
        assertEquals(100.0, result, 0.01);
    }

    @Test
    void calculateMaxShouldReturnPartial(){
        List<Activity> activities = List.of(
                activity(ActivityType.TE_MID,   0),
                activity(ActivityType.TE_FINAL, 0)
        );

        double result = gradeService.calculateMaxPossible(activities);
        assertEquals(40.0, result, 0.01);
    }
}
