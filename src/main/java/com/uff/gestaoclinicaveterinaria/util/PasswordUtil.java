package com.uff.gestaoclinicaveterinaria.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    public static String gerarSalt() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar salt.", e);
        }
    }

    public static String gerarHash(String senha, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((senha + salt).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash.", e);
        }
    }

    public static boolean verificarSenha(String senhaDigitada, String salt, String hashSalvo) {
        String hashDigitado = gerarHash(senhaDigitada, salt);
        return hashDigitado.equals(hashSalvo);
    }
}
