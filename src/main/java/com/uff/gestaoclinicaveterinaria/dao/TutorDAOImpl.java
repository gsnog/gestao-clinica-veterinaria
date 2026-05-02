package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TutorDAOImpl implements TutorDAO {

    @Override
    public List<Tutor> listarTodos() {
        List<Tutor> tutores = new ArrayList<>();
        String sql = "SELECT id, nome FROM usuario WHERE role = 'TUTOR'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setId(rs.getLong("id"));
                tutor.setNome(rs.getString("nome"));
                tutores.add(tutor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutores;
    }

    @Override
    public Tutor buscarPorId(Long id) {
        String sql = "SELECT id, nome FROM usuario WHERE id = ? AND role = 'TUTOR'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tutor tutor = new Tutor();
                    tutor.setId(rs.getLong("id"));
                    tutor.setNome(rs.getString("nome"));
                    return tutor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void salvar(Tutor tutor) {
    }

    @Override
    public void atualizar(Tutor tutor) {
    }

    @Override
    public void deletar(Long id) {
    }
}