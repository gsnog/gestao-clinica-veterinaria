package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Base para os controllers JSON de /api/*: centraliza (de)serialização e o
 * formato de resposta combinado com o front (envelope {success, message, fieldErrors}).
 */
public abstract class ApiServlet extends HttpServlet {

    protected static final ObjectMapper MAPPER = criarObjectMapper();

    private static ObjectMapper criarObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    protected void responderJson(HttpServletResponse response, int status, Object corpo) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(response.getWriter(), corpo);
    }

    protected void responderErro(HttpServletResponse response, int status, String mensagem) throws IOException {
        responderErro(response, status, mensagem, null);
    }

    protected void responderErro(HttpServletResponse response, int status, String mensagem, Map<String, String> fieldErrors) throws IOException {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("success", false);
        corpo.put("message", mensagem);
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            corpo.put("fieldErrors", fieldErrors);
        }
        responderJson(response, status, corpo);
    }

    protected static Map<String, Object> corpoJson(Object... pares) {
        Map<String, Object> mapa = new LinkedHashMap<>();
        for (int i = 0; i < pares.length; i += 2) {
            mapa.put((String) pares[i], pares[i + 1]);
        }
        return mapa;
    }

    protected <T> T lerCorpo(HttpServletRequest request, Class<T> tipo) throws IOException {
        return MAPPER.readValue(request.getReader(), tipo);
    }

    protected Long idUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (Long) session.getAttribute("usuarioId") : null;
    }

    protected String roleUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute("usuarioRole") : null;
    }

    /**
     * Extrai o id numérico do final do path (ex.: /api/pets/100 -> 100).
     * Retorna null quando não há segmento de path ou ele não é um id positivo válido.
     */
    protected Long extrairIdDoPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 2 || !pathInfo.startsWith("/")) {
            return null;
        }
        try {
            long numero = Long.parseLong(pathInfo.substring(1));
            return numero > 0 ? numero : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
