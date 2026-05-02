package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Usuario;

public interface UsuarioDAO {
    Usuario buscarPorEmail(String email);
    void salvar(Usuario usuario);
    Usuario buscarPorId(Long id);
}