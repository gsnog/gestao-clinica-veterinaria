package com.uff.gestaoclinicaveterinaria.util;

public class InputValidator {

    public static boolean isNullOrBlank(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static boolean emailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean nomeCompletoValido(String nome) {
        if (nome == null) {
            return false;
        }

        String[] partes = nome.trim().split("\\s+");
        if (partes.length < 2) {
            return false;
        }

        for (String parte : partes) {
            if (parte.length() < 2) {
                return false;
            }
        }

        return true;
    }

    public static boolean telefoneValido(String telefone) {
        return telefone != null && telefone.matches("\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}");
    }

    public static boolean codigoRecuperacaoValido(String codigo) {
        return codigo != null && codigo.matches("^[0-9]{6}$");
    }

    public static boolean senhaSegura(String senha) {
        if (senha == null) {
            return false;
        }
        return senha.length() >= 8
                && senha.matches(".*[A-Z].*")
                && senha.matches(".*[a-z].*")
                && senha.matches(".*[0-9].*");
    }
}