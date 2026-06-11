package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;

import com.uff.gestaoclinicaveterinaria.util.CsrfUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/csrf")
public class ApiCsrfServlet extends ApiServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        String csrfToken = CsrfUtil.obterToken(session);

        responderJson(response, HttpServletResponse.SC_OK, corpoJson(
                "success", true,
                "csrfToken", csrfToken
        ));
    }
}
