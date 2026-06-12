package org.ifsp.scholardesktop.dao.interfaces;

import org.ifsp.scholardesktop.model.Activity;
import org.ifsp.scholardesktop.model.ActivityType;

import java.util.List;

public interface IActivityDAO {

    boolean insert(Activity activity);

    List<Activity> findByStudent(int studentId);

    Activity findByStudentAndType(int studentId, ActivityType type);

    void update(Activity activity);

    void delete(int activityId);
}
