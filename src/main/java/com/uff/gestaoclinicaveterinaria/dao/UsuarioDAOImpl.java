package com.uff.gestaoclinicaveterinaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.uff.gestaoclinicaveterinaria.model.Usuario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

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
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getSalt());
            stmt.setString(5, usuario.getRole());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT id, nome, email, role FROM usuario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setRole(rs.getString("role"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário.", e);
        }
    }

    @Override
    public void atualizarEmail(Long id, String email) {
        String sql = "UPDATE usuario SET email = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar e-mail do usuário.", e);
        }
    }

    @Override
    public void atualizarSenha(Long id, String senhaHash, String salt) {
        String sql = "UPDATE usuario SET senha_hash = ?, salt = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, senhaHash);
            stmt.setString(2, salt);
            stmt.setLong(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar senha do usuário.", e);
        }
    }
}
