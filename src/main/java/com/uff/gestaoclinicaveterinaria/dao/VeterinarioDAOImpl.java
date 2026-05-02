package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAOImpl implements VeterinarioDAO {

    @Override
    public List<Veterinario> listarTodos() {
        List<Veterinario> veterinarios = new ArrayList<>();
        String sql = "SELECT id, nome FROM usuario WHERE role = 'VETERINARIO'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Veterinario vet = new Veterinario();
                vet.setId(rs.getLong("id"));
                vet.setNome(rs.getString("nome"));
                vet.setEspecialidade("Clínico Geral");
                veterinarios.add(vet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veterinarios;
    }

    @Override
    public Veterinario buscarPorId(Long id) {
        String sql = "SELECT id, nome FROM usuario WHERE id = ? AND role = 'VETERINARIO'";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Veterinario vet = new Veterinario();
                    vet.setId(rs.getLong("id"));
                    vet.setNome(rs.getString("nome"));
                    vet.setEspecialidade("Clínico Geral");
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
    }

    @Override
    public void atualizar(Veterinario veterinario) {
    }

    @Override
    public void deletar(Long id) {
    }
}