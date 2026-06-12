package com.uff.gestaoclinicaveterinaria.dto;

/**
 * Corpo das requisições de criação/edição de usuário feitas pelo ADMIN em /api/usuarios.
 * {@code senha} é obrigatório na criação e opcional na edição (preenchido = redefinir senha).
 */
public record UsuarioAdminRequestDTO(Long id, String nome, String email, String senha, String role) {
}
