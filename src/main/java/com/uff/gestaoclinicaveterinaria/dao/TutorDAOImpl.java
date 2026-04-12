package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDAOImpl implements TutorDAO {

    @Override
    public void salvar(Tutor tutor) {
        String sql = "INSERT INTO tutor (nome, telefone) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getTelefone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Tutor> listarTodos() {
        List<Tutor> tutores = new ArrayList<>();
        String sql = "SELECT * FROM tutor";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Tutor tutor = new Tutor();
                tutor.setId(rs.getLong("id"));
                tutor.setNome(rs.getString("nome"));
                tutor.setTelefone(rs.getString("telefone"));
                tutores.add(tutor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutores;
    }

    @Override
    public Tutor buscarPorId(Long id) {
        String sql = "SELECT * FROM tutor WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tutor tutor = new Tutor();
                    tutor.setId(rs.getLong("id"));
                    tutor.setNome(rs.getString("nome"));
                    tutor.setTelefone(rs.getString("telefone"));
                    return tutor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void atualizar(Tutor tutor) {
        String sql = "UPDATE tutor SET nome = ?, telefone = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getTelefone());
            stmt.setLong(3, tutor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM tutor WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}