package org.ifsp.scholardesktop.dao.interfaces;

import org.ifsp.scholardesktop.model.School;

public interface ISchoolDAO {

    boolean insert(School school);

    School findByName(String name);

}
