package com.uff.gestaoclinicaveterinaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    public void atualizarNome(Long id, String nome) {
        String sql = "UPDATE usuario SET nome = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar nome do usuário.", e);
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

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT id, nome, email, role FROM usuario ORDER BY nome";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setRole(rs.getString("role"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }

        return usuarios;
    }

    @Override
    public void atualizarRole(Long id, String role) {
        String sql = "UPDATE usuario SET role = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar papel do usuário.", e);
        }
    }

    @Override
    public long contarPorRole(String role) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE role = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar usuários por papel.", e);
        }
        return 0;
    }
}