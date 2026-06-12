package org.ifsp.scholardesktop.dao.interfaces;

import org.ifsp.scholardesktop.model.Student;

import java.util.List;

public interface IStudentDAO {

    boolean insert(Student student);

    List<Student> findByClassGroup(int classGroupId);

    Student findById(int studentId);

    void update(Student student);

    void delete(int studentId);
}
