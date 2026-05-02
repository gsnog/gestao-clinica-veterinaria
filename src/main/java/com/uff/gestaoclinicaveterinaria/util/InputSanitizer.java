package com.uff.gestaoclinicaveterinaria.util;

public class InputSanitizer {

    public static String sanitizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        return valor
                .replaceAll("(?i)<script.*?>.*?</script>", "")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
