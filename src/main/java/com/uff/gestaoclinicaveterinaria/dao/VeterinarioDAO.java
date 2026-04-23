package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Veterinario;

import java.util.List;

public interface VeterinarioDAO {
    public void salvar(Veterinario veterinario);
    public List<Veterinario> listarTodos();
    public Veterinario buscarPorId(Long id);
    public void atualizar(Veterinario veterinario);
    public void deletar(Long id);
}
