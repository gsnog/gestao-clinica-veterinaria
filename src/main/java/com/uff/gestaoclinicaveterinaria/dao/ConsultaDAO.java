package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Consulta;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaDAO {
    void salvar(Consulta consulta);
    void atualizar(Consulta consulta);
    void deletar(Long id);
    List<Consulta> listarTodos();
    Consulta buscarPorId(Long id);
    List<Consulta> buscarPorPet(Long petId);
    List<Consulta> buscarPorData(Long veterinarioId, LocalDate data);
    List<Consulta> filtrar(String busca, LocalDate data);
    List<Consulta> buscarPorTutor(Long tutorId);
}