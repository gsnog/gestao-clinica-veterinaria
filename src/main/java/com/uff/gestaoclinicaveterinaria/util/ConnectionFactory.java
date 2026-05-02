package com.uff.gestaoclinicaveterinaria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
            System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/clinica";
    private static final String USUARIO =
            System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
    private static final String SENHA =
            System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "ElixirSuperPoderosos987";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Motorista (Driver) do banco não encontrado!", e);
        }
    }

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados!", e);
        }
    }
}