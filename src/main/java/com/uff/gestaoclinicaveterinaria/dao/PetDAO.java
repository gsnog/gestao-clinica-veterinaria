package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Pet;

import java.util.List;

public interface PetDAO {
    void salvar(Pet pet);
    void atualizar(Pet pet);
    void deletar(Long id);
    List<Pet> listarTodos();
    Pet buscarPorId(Long id);
    List<Pet> buscarPorTutor(Long tutorId);
}
