package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Tutor;

import java.util.List;

public interface TutorDAO {
    public void salvar(Tutor tutor);
    public List<Tutor> listarTodos();
    public Tutor buscarPorId(Long id);
    public void atualizar(Tutor tutor);
    public void deletar(Long id);
}
