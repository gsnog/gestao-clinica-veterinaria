package com.uff.gestaoclinicaveterinaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

public class TutorDAOImpl implements TutorDAO {

    @Override
    public List<Tutor> listarTodos() {
        List<Tutor> tutores = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, t.telefone "
                + "FROM usuario u LEFT JOIN tutor t ON t.usuario_id = u.id "
                + "WHERE u.role = 'TUTOR'";
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
        String sql = "SELECT u.id, u.nome, t.telefone "
                + "FROM usuario u LEFT JOIN tutor t ON t.usuario_id = u.id "
                + "WHERE u.id = ? AND u.role = 'TUTOR'";
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
    public void salvar(Tutor tutor) {
        String sql = "INSERT INTO tutor (usuario_id, telefone) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, tutor.getId());
            stmt.setString(2, tutor.getTelefone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar tutor.", e);
        }
    }

    @Override
    public void atualizar(Tutor tutor) {
        String sqlUsuario = "UPDATE usuario SET nome = ? WHERE id = ? AND role = 'TUTOR'";
        String sqlTutor = "INSERT INTO tutor (usuario_id, telefone) VALUES (?, ?) "
                + "ON CONFLICT (usuario_id) DO UPDATE SET telefone = EXCLUDED.telefone";
        try (Connection conn = ConnectionFactory.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, tutor.getNome());
                stmt.setLong(2, tutor.getId());
                stmt.executeUpdate();
            }
            if (tutor.getTelefone() != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlTutor)) {
                    stmt.setLong(1, tutor.getId());
                    stmt.setString(2, tutor.getTelefone());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ? AND role = 'TUTOR'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}