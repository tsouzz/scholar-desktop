package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IClassGroupDAO;
import org.ifsp.scholardesktop.dao.interfaces.IStudentDAO;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Student;

import java.util.List;

public class StudentService {

    private final IStudentDAO studentDAO;
    private final IClassGroupDAO classGroupDAO;

    public StudentService(IStudentDAO studentDAO, IClassGroupDAO classGroupDAO) {
        this.studentDAO = studentDAO;
        this.classGroupDAO = classGroupDAO;
    }

    public Student createStudent(String name, ClassGroup classGroup) {
        Student student = new Student(name, classGroup);

        studentDAO.insert(student);

        return student;
    }

    public List<Student> findByClassGroup(int classGroupId) {
        return studentDAO.findByClassGroup(classGroupId);
    }

    public Student findById(int id) {
        return studentDAO.findById(id);
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public void deleteStudent(int studentId) {
        studentDAO.delete(studentId);
    }

    public Student transferStudent(
            int studentId,
            int classGroupId
    ) throws InvalidOperationException {
        Student student = studentDAO.findById(studentId);
        ClassGroup classGroup = classGroupDAO.findById(classGroupId);

        if (student.getClassGroup().getModule() != classGroup.getModule()) {
            throw new InvalidOperationException("Transferência inválida: a turma destino pertence a um módulo diferente.");
        }

        student.setClassGroup(classGroup);
        studentDAO.update(student);
        return student;
    }

}