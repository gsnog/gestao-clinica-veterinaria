package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.DashboardDTO;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public DashboardDTO obterEstatisticas() {
        DashboardDTO dto = new DashboardDTO();
        String sql = "SELECT * FROM v_estatisticas_dashboard";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                dto.setTotalTutores(rs.getLong("total_tutores"));
                dto.setTotalPets(rs.getLong("total_pets"));
                dto.setTotalVeterinarios(rs.getLong("total_veterinarios"));
                dto.setTotalConsultas(rs.getLong("total_consultas"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
}