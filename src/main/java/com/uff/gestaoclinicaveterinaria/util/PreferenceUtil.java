package com.uff.gestaoclinicaveterinaria.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utilitário para gerenciar cookies de preferência do usuário
 * (ex.: densidade de tabela, idioma, tema)
 */
public class PreferenceUtil {

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365; // 1 ano
    private static final String COOKIE_PATH = "/";

    /**
     * Salva uma preferência em cookie
     * @param response HttpServletResponse para adicionar o cookie
     * @param key Nome da preferência (ex: "tableDensity", "language")
     * @param value Valor da preferência
     */
    public static void setPreference(HttpServletResponse response, String key, String value) {
        if (key == null || value == null) return;

        Cookie cookie = new Cookie("pref_" + key, value);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath(COOKIE_PATH);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Recupera uma preferência do cookie
     * @param request HttpServletRequest para acessar cookies
     * @param key Nome da preferência
     * @param defaultValue Valor padrão caso não exista
     * @return Valor da preferência ou padrão
     */
    public static String getPreference(HttpServletRequest request, String key, String defaultValue) {
        if (key == null) return defaultValue;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String cookieName = "pref_" + key;
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return defaultValue;
    }

    /**
     * Remove uma preferência
     * @param response HttpServletResponse para remover o cookie
     * @param key Nome da preferência
     */
    public static void removePreference(HttpServletResponse response, String key) {
        if (key == null) return;

        Cookie cookie = new Cookie("pref_" + key, "");
        cookie.setMaxAge(0);
        cookie.setPath(COOKIE_PATH);
        response.addCookie(cookie);
    }
}
