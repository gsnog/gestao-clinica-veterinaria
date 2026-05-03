package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAO;
import com.uff.gestaoclinicaveterinaria.dao.VeterinarioDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.SearchFilterUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/veterinarios")
public class VeterinarioServlet extends HttpServlet {

    private VeterinarioDAO veterinarioDao = new VeterinarioDAOImpl();

    private List<Veterinario> filtrarVeterinarios(List<Veterinario> veterinarios, String busca) {
        String buscaNormalizada = SearchFilterUtil.normalize(busca);
        if (buscaNormalizada.isEmpty()) {
            return veterinarios;
        }

        List<Veterinario> filtrados = new ArrayList<>();
        for (Veterinario vet : veterinarios) {
            if (SearchFilterUtil.startsWithNormalized(vet.getNome(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(vet.getCrmv(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(vet.getEspecialidade(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(String.valueOf(vet.getId()), buscaNormalizada)) {
                filtrados.add(vet);
            }
        }

        return filtrados;
    }

    private void encaminharListaVeterinarios(HttpServletRequest request,
                                             HttpServletResponse response,
                                             List<Veterinario> lista,
                                             Long idLogado) throws ServletException, IOException {
        Veterinario vetLogado = null;
        List<Veterinario> outrosVeterinarios = new ArrayList<>();

        for (Veterinario vet : lista) {
            if (vet == null || vet.getId() == null) {
                continue;
            }
            if (idLogado != null && vet.getId().equals(idLogado)) {
                vetLogado = vet;
                continue;
            }
            outrosVeterinarios.add(vet);
        }

        request.setAttribute("vetLogado", vetLogado);
        request.setAttribute("listaDeVeterinarios", outrosVeterinarios);
        request.setAttribute("buscaParam", request.getParameter("busca") != null ? request.getParameter("busca") : "");
        request.getRequestDispatcher("/lista-veterinarios.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if (acao != null && acao.equals("editar")) {
            Long id = Long.parseLong(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;
            if (idLogado == null || !idLogado.equals(id)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            Veterinario vet = veterinarioDao.buscarPorId(id);
            if (vet == null) {
                response.sendRedirect(request.getContextPath() + "/veterinarios");
                return;
            }
            request.setAttribute("veterinario", vet);
            request.getRequestDispatcher("/form-veterinario.jsp").forward(request, response);

        } else {
            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;

            List<Veterinario> lista = veterinarioDao.listarTodos();
            lista = filtrarVeterinarios(lista, request.getParameter("busca"));

            encaminharListaVeterinarios(request, response, lista, idLogado);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;
            if (idLogado == null || !idLogado.equals(id)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            veterinarioDao.deletar(id);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Pega os dados do formulário
        String nome = request.getParameter("nomeVet");
        String crmv = request.getParameter("crmvVet");
        String especialidade = request.getParameter("especialidadeVet");

        if (acao != null && acao.equals("atualizar")) {
            Long id = Long.parseLong(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            Long idLogado = session != null ? (Long) session.getAttribute("usuarioId") : null;
            if (idLogado == null || !idLogado.equals(id)) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            Veterinario vet = new Veterinario();
            vet.setId(id);
            vet.setNome(nome);
            vet.setCrmv(crmv);
            vet.setEspecialidade(especialidade);

            veterinarioDao.atualizar(vet);
        }

        response.sendRedirect(request.getContextPath() + "/veterinarios");
    }
}