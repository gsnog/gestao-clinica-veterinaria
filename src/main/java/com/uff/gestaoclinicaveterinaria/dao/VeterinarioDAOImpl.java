package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAOImpl implements VeterinarioDAO {

    @Override
    public void salvar(Veterinario veterinario) {
        String sql = "INSERT INTO veterinario (nome, crmv, especialidade) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, veterinario.getNome());
            stmt.setString(2, veterinario.getCrmv());
            stmt.setString(3, veterinario.getEspecialidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Veterinario> listarTodos() {
        List<Veterinario> veterinarios = new ArrayList<>();
        String sql = "SELECT * FROM veterinario";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Veterinario vet = new Veterinario();
                vet.setId(rs.getLong("id"));
                vet.setNome(rs.getString("nome"));
                vet.setCrmv(rs.getString("crmv"));
                vet.setEspecialidade(rs.getString("especialidade"));
                veterinarios.add(vet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veterinarios;
    }

    @Override
    public Veterinario buscarPorId(Long id) {
        String sql = "SELECT * FROM veterinario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Veterinario vet = new Veterinario();
                    vet.setId(rs.getLong("id"));
                    vet.setNome(rs.getString("nome"));
                    vet.setCrmv(rs.getString("crmv"));
                    vet.setEspecialidade(rs.getString("especialidade"));
                    return vet;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void atualizar(Veterinario veterinario) {
        String sql = "UPDATE veterinario SET nome = ?, crmv = ?, especialidade = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, veterinario.getNome());
            stmt.setString(2, veterinario.getCrmv());
            stmt.setString(3, veterinario.getEspecialidade());
            stmt.setLong(4, veterinario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM veterinario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}