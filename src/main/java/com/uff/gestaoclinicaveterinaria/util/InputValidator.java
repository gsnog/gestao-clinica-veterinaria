package com.uff.gestaoclinicaveterinaria.util;

public class InputValidator {

    public static boolean isNullOrBlank(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static boolean emailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}