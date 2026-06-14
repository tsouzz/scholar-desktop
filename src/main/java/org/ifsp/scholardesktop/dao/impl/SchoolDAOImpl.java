package org.ifsp.scholardesktop.dao.impl;

import org.ifsp.scholardesktop.dao.DBConnection;
import org.ifsp.scholardesktop.dao.interfaces.ISchoolDAO;
import org.ifsp.scholardesktop.model.School;

import java.sql.*;

public class SchoolDAOImpl implements ISchoolDAO {

    @Override
    public boolean insert(School school) {
        String sql = "INSERT INTO schools (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, school.getName());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    school.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir escola: " + e.getMessage());
        }
    }

    @Override
    public School findByName(String name) {
        String sql = "SELECT * FROM schools WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    School school = new School(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                    return school;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar escola:"  + e.getMessage());
        }

        return null;
    }
}
