package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.IClassGroupDAO;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.model.Teacher;

public class ClassGroupService {

    private final IClassGroupDAO classGroupDAO;

    public ClassGroupService(IClassGroupDAO classGroupDAO) {
        this.classGroupDAO = classGroupDAO;
    }

    public ClassGroup createClassGroup(Module module, Teacher teacher) {
        int number = nextNumber(module, teacher.getId());
        // ClassGroup classGroup = new ClassGroup(number, module, teacher);
    }

    private int nextNumber(Module module, int teacherId) {
        int count = classGroupDAO.countByModuleAndTeacher(module, teacherId);
        return count + 1;
    }
}
