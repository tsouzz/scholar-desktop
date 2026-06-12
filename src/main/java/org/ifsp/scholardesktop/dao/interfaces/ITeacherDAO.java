package org.ifsp.scholardesktop.dao.interfaces;

import org.ifsp.scholardesktop.model.Teacher;

public interface ITeacherDAO {

    boolean insert(Teacher teacher);

    Teacher findByEmail(String email);

    Teacher findById(int teacherId);

    void update(Teacher teacher);

    void delete(int teacherId);
}
