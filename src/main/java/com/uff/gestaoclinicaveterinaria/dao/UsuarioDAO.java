package com.uff.gestaoclinicaveterinaria.dao;

import java.util.List;

import com.uff.gestaoclinicaveterinaria.model.Usuario;

public interface UsuarioDAO {
    Usuario buscarPorEmail(String email);
    void salvar(Usuario usuario);
    Usuario buscarPorId(Long id);
    void deletar(Long id);
    void atualizarEmail(Long id, String email);
    void atualizarNome(Long id, String nome);
    void atualizarSenha(Long id, String senhaHash, String salt);
    List<Usuario> listarTodos();
    void atualizarRole(Long id, String role);
    long contarPorRole(String role);
}