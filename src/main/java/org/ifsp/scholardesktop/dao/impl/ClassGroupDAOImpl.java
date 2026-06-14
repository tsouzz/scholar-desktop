package org.ifsp.scholardesktop.dao.impl;

import org.ifsp.scholardesktop.dao.DBConnection;
import org.ifsp.scholardesktop.dao.interfaces.IClassGroupDAO;
import org.ifsp.scholardesktop.model.ClassGroup;
import org.ifsp.scholardesktop.model.Module;
import org.ifsp.scholardesktop.model.School;
import org.ifsp.scholardesktop.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassGroupDAOImpl implements IClassGroupDAO {

    @Override
    public boolean insert(ClassGroup classGroup) {
        String sql = "INSERT INTO class_groups (number, module, teacher_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setInt(1, classGroup.getNumber());
            stmt.setString(2, classGroup.getModule().name());
            stmt.setInt(3, classGroup.getTeacher().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    classGroup.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Turma já existe para este módulo e professor.");
        } catch (SQLException e){
            throw new RuntimeException("Erro ao inserir turma: " + e.getMessage());
        }
    }

    @Override
    public List<ClassGroup> findByTeacher(int teacherId) {
        List<ClassGroup> classGroups = new ArrayList<>();

        String sql = """
                    SELECT
                        cg.id            AS class_group_id,
                        cg.number,
                        cg.module,
                        t.id             AS teacher_id,
                        t.name           AS teacher_name,
                        t.email          AS teacher_email,
                        t.password_hash,
                        s.id             AS school_id,
                        s.name           AS school_name
                    FROM class_groups cg
                    JOIN teachers t ON cg.teacher_id = t.id
                    JOIN schools  s ON t.school_id   = s.id
                    WHERE cg.teacher_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
             ) {

            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classGroups.add(mapRow(rs));
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar as classes.");
        }

        return classGroups;
    }

    @Override
    public ClassGroup findById(int classGroupId) {
        String sql = """
                SELECT
                    cg.id            AS class_group_id,
                    cg.number,
                    cg.module,
                    t.id             AS teacher_id,
                    t.name           AS teacher_name,
                    t.email          AS teacher_email,
                    t.password_hash,
                    s.id             AS school_id,
                    s.name           AS school_name
                FROM class_groups cg
                JOIN teachers t ON cg.teacher_id = t.id
                JOIN schools  s ON t.school_id   = s.id
                WHERE cg.id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, classGroupId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar classe.");
        }

        return null;
    }

    @Override
    public int countByModuleAndTeacher(Module module, int teacherId) {
        String sql = "SELECT COUNT(*) FROM class_groups WHERE module = ? AND teacher_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, module.name());
            stmt.setInt(2, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar classes: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public void update(ClassGroup classGroup) {
        String sql = "UPDATE class_groups SET module = ?, teacher_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, classGroup.getModule().name());
            stmt.setInt(2, classGroup.getTeacher().getId());
            stmt.setInt(3, classGroup.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a classe: " + e.getMessage());
        }
    }

    @Override
    public void delete(int classGroupId) {
         String sql = "DELETE FROM class_groups WHERE id = ?";

         try (Connection conn = DBConnection.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setInt(1, classGroupId);
             stmt.executeUpdate();

         } catch (SQLException e) {
             throw new RuntimeException("Erro ao deletar a classe: " + e.getMessage());
         }
    }

    private ClassGroup mapRow(ResultSet rs) throws SQLException {
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
                rs.getInt("number"),
                Module.valueOf(rs.getString("module")),
                teacher
        );
        classGroup.setId(rs.getInt("class_group_id"));

        return classGroup;
    }
}