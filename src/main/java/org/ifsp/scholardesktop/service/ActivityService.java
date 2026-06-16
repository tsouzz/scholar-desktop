package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IActivityDAO;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.Activity;
import org.ifsp.scholardesktop.model.ActivityType;
import org.ifsp.scholardesktop.model.Student;

import java.time.LocalDate;
import java.util.List;

public class ActivityService {

    private final IActivityDAO activityDAO;

    public ActivityService(IActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    public Activity createActivity(
            ActivityType type,
            double grade,
            Student student
    ) throws InvalidOperationException {

        if (grade < 0 || grade > 10) {
            throw new InvalidOperationException("A nota deve estar entre 0 e 10.");
        }

        Activity existing = activityDAO.findByStudentAndType(student.getId(), type);
        if (existing != null) {
            throw new InvalidOperationException("Atividade " + type.getLabel() + " já registrada para este aluno.");
        }

        Activity activity = new Activity(
                type,
                grade,
                LocalDate.now(),
                student
        );

        activityDAO.insert(activity);
        return activity;
    }

    public List<Activity> findByStudent(int studentId) {
        return activityDAO.findByStudent(studentId);
    }

    public Activity findByStudentAndType(int studentId, ActivityType type) {
        return activityDAO.findByStudentAndType(studentId, type);
    }

    public void update(Activity activity) {
        activityDAO.update(activity);
    }

    public void delete(int activityId) {
        activityDAO.delete(activityId);
    }
}
