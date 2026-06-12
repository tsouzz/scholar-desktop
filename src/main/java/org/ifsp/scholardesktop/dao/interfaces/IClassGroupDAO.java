package org.ifsp.scholardesktop.dao.interfaces;

import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;

import java.util.List;

public interface IClassGroupDAO {

    boolean insert(ClassGroup classGroup);

    List<ClassGroup> findByTeacher(int teacherId);

    ClassGroup findById(int classGroupId);

    int countByModuleAndTeacher(Module module, int teacherId);

    void update(ClassGroup classGroup);

    void delete(int classGroupId);
}
