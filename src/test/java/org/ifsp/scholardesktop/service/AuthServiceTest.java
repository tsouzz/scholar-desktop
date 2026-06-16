package org.ifsp.scholardesktop.service;

import org.ifsp.scholardesktop.dao.interfaces.ISchoolDAO;
import org.ifsp.scholardesktop.dao.interfaces.ITeacherDAO;
import org.ifsp.scholardesktop.exception.InvalidCredentialsException;
import org.ifsp.scholardesktop.exception.InvalidOperationException;
import org.ifsp.scholardesktop.model.School;
import org.ifsp.scholardesktop.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private ITeacherDAO teacherDAO;

    @Mock
    private ISchoolDAO schoolDAO;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(teacherDAO, schoolDAO);
    }

    @Test
    void registerShouldThrow_WhenPasswordTooShort() {
        assertThrows(InvalidOperationException.class, () ->
                authService.register("Thulio", "thulio@email.com", "Ab1", "Ab1", "CNA")
        );
    }

    @Test
    void registerShouldThrow_WhenPasswordHasNoUppercase() {
        assertThrows(InvalidOperationException.class, () ->
                authService.register("Thulio", "thulio@email.com", "abcdefg1", "abcdefg1", "CNA")
        );
    }

    @Test
    void registerShouldThrow_WhenPasswordHasNoNumber() {
        assertThrows(InvalidOperationException.class, () ->
                authService.register("Thulio", "thulio@email.com", "Abcdefgh", "Abcdefgh", "CNA")
        );
    }

    @Test
    void registerShouldThrow_WhenPasswordsDoNotMatch() {
        assertThrows(InvalidOperationException.class, () ->
                authService.register("Thulio", "thulio@email.com", "Abcdef1!", "Abcdef2!", "CNA")
        );
    }

    @Test
    void registerShouldCreateSchool_WhenSchoolDoesNotExist() throws InvalidOperationException {
        when(schoolDAO.findByName("CNA")).thenReturn(null);

        authService.register("Thulio", "thulio@email.com", "Abcdef1!", "Abcdef1!", "CNA");

        verify(schoolDAO, times(1)).insert(any(School.class));
    }

    @Test
    void registerShouldReuseSchool_WhenSchoolAlreadyExists() throws InvalidOperationException {
        School existingSchool = new School(1, "CNA");
        when(schoolDAO.findByName("CNA")).thenReturn(existingSchool);

        authService.register("Thulio", "thulio@email.com", "Abcdef1!", "Abcdef1!", "CNA");

        verify(schoolDAO, never()).insert(any(School.class));
        verify(teacherDAO, times(1)).insert(any(Teacher.class));
    }

    @Test
    void loginShouldThrow_WhenEmailNotFound() {
        when(teacherDAO.findByEmail("notfound@email.com")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login("notfound@email.com", "Abcdef1!")
        );
    }

    @Test
    void loginShouldThrow_WhenPasswordIsWrong() {
        String hash = BCrypt.hashpw("Abcdef1!", BCrypt.gensalt());
        Teacher teacher = new Teacher(1, "Thulio", "thulio@email.com", hash, new School(1, "CNA"));
        when(teacherDAO.findByEmail("thulio@email.com")).thenReturn(teacher);

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login("thulio@email.com", "WrongPass1!")
        );
    }

    @Test
    void loginShouldReturnTeacher_WhenCredentialsAreValid() throws InvalidOperationException {
        String hash = BCrypt.hashpw("Abcdef1!", BCrypt.gensalt());
        Teacher teacher = new Teacher(1, "Thulio", "thulio@email.com", hash, new School(1, "CNA"));
        when(teacherDAO.findByEmail("thulio@email.com")).thenReturn(teacher);

        Teacher result = authService.login("thulio@email.com", "Abcdef1!");

        assertNotNull(result);
        assertEquals("thulio@email.com", result.getEmail());
    }
}