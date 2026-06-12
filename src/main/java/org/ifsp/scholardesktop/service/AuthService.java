package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.impl.TeacherDAOImpl;
import org.ifsp.scholardesktop.dao.interfaces.ISchoolDAO;
import org.ifsp.scholardesktop.dao.interfaces.ITeacherDAO;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.Teacher;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final ITeacherDAO teacherDAO;
    private final ISchoolDAO schoolDAO;

    public AuthService(ITeacherDAO teacherDAO, ISchoolDAO schoolDAO) {
        this.teacherDAO = teacherDAO;
        this.schoolDAO = schoolDAO;
    }

    public Teacher register(
            String teacherName,
            String email,
            String rawPassword,
            String confirmPassword,
            String schoolName
    ) throws InvalidOperationException {



        return null;
    }

    public Teacher login(
            String email,
            String rawPassword
    ) throws InvalidOperationException {

        return null;
    }

    private void validatePasswordMatch(
            String rawPassword,
            String confirmPassword
    ) throws InvalidOperationException {

        if (!rawPassword.equals(confirmPassword)) {
            throw new InvalidOperationException("Senhas diferentes.");
        }

    }

    private void validatePasswordStrength(String rawPassword) throws InvalidOperationException {
        if (rawPassword.length() < 8) {
            throw new InvalidOperationException("A senha deve conter pelo menos 8 caracteres.");
        }
        if (!rawPassword.matches(".*[A-Z].*")) {
            throw new InvalidOperationException("A senha deve conter pelo menos uma letra maiúscula.");
        }
        if (!rawPassword.matches(".*[0-9].*")) {
            throw new InvalidOperationException("A senha deve conter pelo menos um número.");
        }
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    private boolean checkPassword(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}
