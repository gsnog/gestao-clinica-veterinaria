package com.uff.gestaoclinicaveterinaria.dto;

public record RegistroRequestDTO(String nome, String email, String senha, String role,
                                 String telefone, String crmv, String especialidade) {
}
