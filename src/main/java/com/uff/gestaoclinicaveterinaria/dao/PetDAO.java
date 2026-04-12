package com.uff.gestaoclinicaveterinaria.dto;

import com.uff.gestaoclinicaveterinaria.model.Pet;

import java.util.List;

public interface PetDAO {
    public void salvar(Pet pet);
    public List<Pet> listarTodos();
    public Pet buscarPorId(Long id);
    public void atualizar(Pet pet);
    public void deletar(Long id);
}
