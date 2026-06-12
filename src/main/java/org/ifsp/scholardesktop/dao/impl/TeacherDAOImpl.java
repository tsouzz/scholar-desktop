package org.ifsp.scholardesktop.dao.impl;

import org.ifsp.scholardesktop.dao.DBConnection;
import org.ifsp.scholardesktop.dao.interfaces.ITeacherDAO;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.model.School;
import org.ifsp.scholardesktop.model.Teacher;

import java.sql.*;

public class TeacherDAOImpl implements ITeacherDAO {

    @Override
    public boolean insert(Teacher teacher) {
        String sql = "INSERT INTO teachers (name, email, password_hash, school_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, teacher.getName());
            stmt.setString(2, teacher.getEmail());
            stmt.setString(3, teacher.getPasswordHash());
            stmt.setInt(4, teacher.getSchool().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    teacher.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Email já cadastrado.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir professor: " + e.getMessage());
        }
    }

    @Override
    public Teacher findByEmail(String email) {
        String sql = """
                SELECT
                    t.id             AS teacher_id,
                    t.name           AS teacher_name,
                    t.email          AS teacher_email,
                    t.password_hash,
                    s.id             AS school_id,
                    s.name           AS school_name
                FROM teachers t
                JOIN schools s ON t.school_id = s.id
                WHERE t.email = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar professor: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Teacher findById(int teacherId) {
        String sql = """
                SELECT
                    t.id             AS teacher_id,
                    t.name           AS teacher_name,
                    t.email          AS teacher_email,
                    t.password_hash,
                    s.id             AS school_id,
                    s.name           AS school_name
                FROM teachers t
                JOIN schools s ON t.school_id = s.id
                WHERE t.id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar professor: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void update(Teacher teacher) {
        String sql = "UPDATE teachers SET name = ?, email = ?, password_hash = ?, school_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacher.getName());
            stmt.setString(2, teacher.getEmail());
            stmt.setString(3, teacher.getPasswordHash());
            stmt.setInt(4, teacher.getSchool().getId());
            stmt.setInt(5, teacher.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o professor: " + e.getMessage());
        }
    }

    @Override
    public void delete(int teacherId) {
        String sql = "DELETE FROM teachers WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o professor: " + e.getMessage());
        }
    }

    private Teacher mapRow(ResultSet rs) throws SQLException {
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

        return teacher;
    }
}
