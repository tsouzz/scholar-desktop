package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IClassGroupDAO;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.model.Teacher;

import java.util.List;

public class ClassGroupService {

    private final IClassGroupDAO classGroupDAO;

    public ClassGroupService(IClassGroupDAO classGroupDAO) {
        this.classGroupDAO = classGroupDAO;
    }

    public ClassGroup createClassGroup(Module module, Teacher teacher) {
        int number = nextNumber(module, teacher.getId());

        ClassGroup classGroup = new ClassGroup(number, module, teacher);

        classGroupDAO.insert(classGroup);

        return classGroup;
    }

    public List<ClassGroup> getClassesByTeacher(int teacherId) {
        return classGroupDAO.findByTeacher(teacherId);
    }

    public ClassGroup getClassGroupById(int classGroupId) {
        return classGroupDAO.findById(classGroupId);
    }

    public ClassGroup updateModule(int classGroupId, Module newModule) {
        ClassGroup classGroup = classGroupDAO.findById(classGroupId);
        int number = nextNumber(newModule, classGroup.getTeacher().getId());
        classGroup.setModule(newModule);
        classGroup.setNumber(number);
        classGroupDAO.update(classGroup);
        return classGroup;
    }

    public void deleteClassGroup(int classGroupId) {
        classGroupDAO.delete(classGroupId);
    }

    private int nextNumber(Module module, int teacherId) {
        int count = classGroupDAO.countByModuleAndTeacher(module, teacherId);
        return count + 1;
    }
}
