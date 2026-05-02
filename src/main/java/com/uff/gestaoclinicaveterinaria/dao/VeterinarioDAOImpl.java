package com.uff.gestaoclinicaveterinaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

public class VeterinarioDAOImpl implements VeterinarioDAO {

    @Override
    public List<Veterinario> listarTodos() {
        List<Veterinario> veterinarios = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, v.crmv, v.especialidade "
                + "FROM usuario u LEFT JOIN veterinario v ON v.usuario_id = u.id "
                + "WHERE u.role = 'VETERINARIO'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Veterinario vet = new Veterinario();
                vet.setId(rs.getLong("id"));
                vet.setNome(rs.getString("nome"));
                vet.setCrmv(rs.getString("crmv"));
                String esp = rs.getString("especialidade");
                vet.setEspecialidade(esp != null ? esp : "Clínico Geral");
                veterinarios.add(vet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veterinarios;
    }

    @Override
    public Veterinario buscarPorId(Long id) {
        String sql = "SELECT u.id, u.nome, v.crmv, v.especialidade "
                + "FROM usuario u LEFT JOIN veterinario v ON v.usuario_id = u.id "
                + "WHERE u.id = ? AND u.role = 'VETERINARIO'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Veterinario vet = new Veterinario();
                    vet.setId(rs.getLong("id"));
                    vet.setNome(rs.getString("nome"));
                    vet.setCrmv(rs.getString("crmv"));
                    String esp = rs.getString("especialidade");
                    vet.setEspecialidade(esp != null ? esp : "Clínico Geral");
                    return vet;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void salvar(Veterinario veterinario) {
        String sql = "INSERT INTO veterinario (usuario_id, crmv, especialidade) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, veterinario.getId());
            stmt.setString(2, veterinario.getCrmv());
            stmt.setString(3, veterinario.getEspecialidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar veterinário.", e);
        }
    }

    @Override
    public void atualizar(Veterinario veterinario) {
        String sqlUsuario = "UPDATE usuario SET nome = ? WHERE id = ? AND role = 'VETERINARIO'";
        String sqlVet = "INSERT INTO veterinario (usuario_id, crmv, especialidade) VALUES (?, ?, ?) "
            + "ON CONFLICT (usuario_id) DO UPDATE SET crmv = EXCLUDED.crmv, especialidade = EXCLUDED.especialidade";
        try (Connection conn = ConnectionFactory.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, veterinario.getNome());
                stmt.setLong(2, veterinario.getId());
                stmt.executeUpdate();
            }
            String crmv = veterinario.getCrmv();
            String esp = veterinario.getEspecialidade();
            if (crmv != null || esp != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlVet)) {
                    stmt.setLong(1, veterinario.getId());
                    stmt.setString(2, crmv != null ? crmv : "");
                    stmt.setString(3, esp != null ? esp : "Clínico Geral");
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ? AND role = 'VETERINARIO'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}