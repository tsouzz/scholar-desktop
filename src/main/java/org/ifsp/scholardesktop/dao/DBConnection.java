package org.ifsp.scholardesktop.dao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.load();

    public static Connection getConnection() {

        final String URL = dotenv.get("DB_URL");
        final String USER =  dotenv.get("DB_USER");
        final String PASSWORD = dotenv.get("DB_PASSWORD");

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados:" + e.getMessage());
        }
    }
}