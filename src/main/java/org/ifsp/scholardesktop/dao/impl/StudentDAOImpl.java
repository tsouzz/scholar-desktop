package org.ifsp.scholardesktop.dao.impl;

import org.ifsp.scholardesktop.dao.DBConnection;
import org.ifsp.scholardesktop.dao.interfaces.IStudentDAO;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.model.School;
import org.ifsp.scholardesktop.model.Student;
import org.ifsp.scholardesktop.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements IStudentDAO {

    @Override
    public boolean insert(Student student) {
        String sql = "INSERT INTO students (name, class_group_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getClassGroup().getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Turma não encontrada para o aluno: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir aluno: " + e.getMessage());
        }
    }

    @Override
    public List<Student> findByClassGroup(int classGroupId) {
        List<Student> students = new ArrayList<>();

        String sql = """
            SELECT
                st.id            AS student_id,
                st.name          AS student_name,
                cg.id            AS class_group_id,
                cg.number,
                cg.module,
                t.id             AS teacher_id,
                t.name           AS teacher_name,
                t.email          AS teacher_email,
                t.password_hash,
                s.id             AS school_id,
                s.name           AS school_name
            FROM students st
            JOIN class_groups cg ON st.class_group_id = cg.id
            JOIN teachers t      ON cg.teacher_id     = t.id
            JOIN schools s       ON t.school_id       = s.id
            WHERE st.class_group_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, classGroupId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar alunos.");
        }

        return students;
    }

    @Override
    public Student findById(int studentId) {
        String sql = """
                SELECT
                    st.id            AS student_id,
                    st.name          AS student_name,
                    cg.id            AS class_group_id,
                    cg.number,
                    cg.module,
                    t.id             AS teacher_id,
                    t.name           AS teacher_name,
                    t.email          AS teacher_email,
                    t.password_hash,
                    s.id             AS school_id,
                    s.name           AS school_name
                FROM students st
                JOIN class_groups cg ON st.class_group_id = cg.id
                JOIN teachers t      ON cg.teacher_id     = t.id
                JOIN schools s       ON t.school_id       = s.id
                WHERE st.id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar aluno.");
        }
        return null;
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE students SET name = ?, class_group_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getClassGroup().getId());
            stmt.setInt(3, student.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a aluno: " + e.getMessage());
        }
    }

    @Override
    public void delete(int studentId) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o aluno: " + e.getMessage());
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        School school = new School(
                rs.getInt("school_id"),
                rs.getString("school_name")
        );

        Teacher teacher = new Teacher(
                rs.getInt("teacher_id"),
                rs.getString("teacher_name"),
                rs.getString("teacher_email"),
                rs.getString("password_hash"),
                school
        );

        ClassGroup classGroup = new ClassGroup(
                rs.getInt("class_group_id"),
                rs.getInt("number"),
                Module.valueOf(rs.getString("module")),
                teacher
        );

        Student student = new Student(
                rs.getString("student_name"),
                classGroup
        );
        student.setId(rs.getInt("student_id"));

        return student;
    }
}
