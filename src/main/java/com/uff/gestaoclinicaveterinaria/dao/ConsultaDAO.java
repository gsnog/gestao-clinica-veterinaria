package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Consulta;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaDAO {
    public void salvar(Consulta consulta);
    public List<Consulta> listarTodos();
    public Consulta buscarPorId(Long id);
    public void atualizar(Consulta consulta);
    public void deletar(Long id);

    List<Consulta> buscarPorPet(Long petId);
    List<Consulta> buscarPorData(Long veterinarioId, LocalDate data);
}
