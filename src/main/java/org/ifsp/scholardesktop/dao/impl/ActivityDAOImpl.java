package org.ifsp.scholardesktop.dao.impl;

import org.ifsp.scholardesktop.dao.DBConnection;
import org.ifsp.scholardesktop.dao.interfaces.IActivityDAO;
import org.ifsp.scholardesktop.model.*;
import org.ifsp.scholardesktop.model.Module;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAOImpl implements IActivityDAO {

    @Override
    public boolean insert(Activity activity) {
        String sql = "INSERT INTO activities (student_id, type, grade, registration_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setInt(1, activity.getStudent().getId());
            stmt.setString(2, activity.getActivityType().name());
            stmt.setDouble(3, activity.getGrade());
            stmt.setDate(4, Date.valueOf(activity.getRegistrationDate()));


            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    activity.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("atividade já existe para este aluno.");
        } catch (SQLException e){
            throw new RuntimeException("Erro ao inserir atividade: " + e.getMessage());
        }
    }

    @Override
    public List<Activity> findByStudent(int studentId) {
        List<Activity> activities = new ArrayList<>();

        String sql = """
                SELECT
                    a.id                AS activity_id,
                    a.type,
                    a.grade,
                    a.registration_date,
                    st.id               AS student_id,
                    st.name             AS student_name,
                    cg.id               AS class_group_id,
                    cg.number,
                    cg.module,
                    t.id                AS teacher_id,
                    t.name              AS teacher_name,
                    t.email             AS teacher_email,
                    t.password_hash,
                    s.id                AS school_id,
                    s.name              AS school_name
                FROM activities a
                JOIN students st      ON a.student_id        = st.id
                JOIN class_groups cg  ON st.class_group_id   = cg.id
                JOIN teachers t       ON cg.teacher_id       = t.id
                JOIN schools s        ON t.school_id         = s.id
                WHERE a.student_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activities.add(mapRow(rs));
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar as atividades.");
        }

        return activities;
    }

    @Override
    public Activity findByStudentAndType(int studentId, ActivityType type) {
        String sql = """
                SELECT
                    a.id                AS activity_id,
                    a.type,
                    a.grade,
                    a.registration_date,
                    st.id               AS student_id,
                    st.name             AS student_name,
                    cg.id               AS class_group_id,
                    cg.number,
                    cg.module,
                    t.id                AS teacher_id,
                    t.name              AS teacher_name,
                    t.email             AS teacher_email,
                    t.password_hash,
                    s.id                AS school_id,
                    s.name              AS school_name
                FROM activities a
                JOIN students st      ON a.student_id        = st.id
                JOIN class_groups cg  ON st.class_group_id   = cg.id
                JOIN teachers t       ON cg.teacher_id       = t.id
                JOIN schools s        ON t.school_id         = s.id
                WHERE a.student_id = ? AND a.type = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, studentId);
            stmt.setString(2, type.name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar atividade.");
        }

        return null;
    }

    @Override
    public void update(Activity activity) {
        String sql = "UPDATE activities SET grade = ?, registration_date = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, activity.getGrade());
            stmt.setDate(2, Date.valueOf(activity.getRegistrationDate()));
            stmt.setInt(3, activity.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a atividade: " + e.getMessage());
        }
    }

    @Override
    public void delete(int activityId) {
        String sql = "DELETE FROM activities WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, activityId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar a atividade: " + e.getMessage());
        }
    }

    private Activity mapRow(ResultSet rs) throws SQLException {
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
                rs.getInt("student_id"),
                rs.getString("student_name"),
                classGroup
        );

        Activity activity = new Activity(
                rs.getInt("activity_id"),
                ActivityType.valueOf(rs.getString("type")),
                rs.getDouble("grade"),
                rs.getDate("registration_date").toLocalDate(),
                student
        );

        return activity;
    }
}
