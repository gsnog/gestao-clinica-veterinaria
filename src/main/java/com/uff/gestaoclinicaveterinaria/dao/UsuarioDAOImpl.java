package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT id, nome, email, senha_hash, salt, role FROM usuario WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenhaHash(rs.getString("senha_hash"));
                    usuario.setSalt(rs.getString("salt"));
                    usuario.setRole(rs.getString("role"));

                    return usuario;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por e-mail.", e);
        }

        return null;
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha_hash, salt, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getSalt());
            stmt.setString(5, usuario.getRole());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }
    }
}
