package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.SearchFilterUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/pets")
public class PetServlet extends HttpServlet {

    private PetDAO petDao = new PetDAOImpl();
    private TutorDAO tutorDAO = new TutorDAOImpl();
    private static final DateTimeFormatter DATA_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("pt-BR"));

    private List<Pet> filtrarPets(List<Pet> pets, String busca) {
        String buscaNormalizada = SearchFilterUtil.normalize(busca);
        if (buscaNormalizada.isEmpty()) {
            return pets;
        }

        List<Pet> filtrados = new ArrayList<>();
        for (Pet pet : pets) {
            if (SearchFilterUtil.startsWithNormalized(pet.getNome(), buscaNormalizada)
                    || SearchFilterUtil.startsWithNormalized(String.valueOf(pet.getId()), buscaNormalizada)) {
                filtrados.add(pet);
            }
        }
        return filtrados;
    }

    private Map<Long, String> montarDatasNascimentoFormatadas(List<Pet> lista) {
        Map<Long, String> datasFormatadas = new HashMap<>();
        if (lista == null) {
            return datasFormatadas;
        }

        for (Pet pet : lista) {
            if (pet == null || pet.getId() == null || pet.getDataNascimento() == null) {
                continue;
            }
            String data = pet.getDataNascimento().format(DATA_FMT).replace(".", "");
            datasFormatadas.put(pet.getId(), data);
        }

        return datasFormatadas;
    }

    private void encaminharListaPets(HttpServletRequest request,
                                     HttpServletResponse response,
                                     List<Pet> lista) throws ServletException, IOException {
        request.setAttribute("listaDePets", lista);
        request.setAttribute("buscaParam", request.getParameter("busca") != null ? request.getParameter("busca") : "");
        request.setAttribute("datasNascimentoFormatadas", montarDatasNascimentoFormatadas(lista));
        request.getRequestDispatcher("/lista-pets.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        Long idLogado = (Long) session.getAttribute("usuarioId");
        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Pet pet = petDao.buscarPorId(id);

            if (pet == null || pet.getTutor() == null) {
                response.sendRedirect(request.getContextPath() + "/pets");
                return;
            }

            if ("TUTOR".equals(role) && !idLogado.equals(pet.getTutor().getId())) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            List<Tutor> tutores = new ArrayList<>();
            if ("TUTOR".equals(role)) {
                tutores.add(tutorDAO.buscarPorId(idLogado));
            } else {
                tutores = tutorDAO.listarTodos();
            }

            request.setAttribute("listaTutores", tutores);
            request.setAttribute("pet", pet);
            request.setAttribute("dataMaxHoje", LocalDate.now().toString());
            request.getRequestDispatcher("/form-pet.jsp").forward(request, response);

        } else if ("novo".equals(acao)) {

            List<Tutor> tutores = new ArrayList<>();
            if ("TUTOR".equals(role)) {
                tutores.add(tutorDAO.buscarPorId(idLogado));
            } else {
                tutores = tutorDAO.listarTodos();
            }

            request.setAttribute("listaTutores", tutores);
            request.setAttribute("dataMaxHoje", LocalDate.now().toString());
            request.getRequestDispatcher("/form-pet.jsp").forward(request, response);

        } else {
            List<Pet> lista;
            if ("TUTOR".equals(role)) {
                lista = petDao.buscarPorTutor(idLogado);
            } else {
                lista = petDao.listarTodos();
            }

            lista = filtrarPets(lista, request.getParameter("busca"));
            encaminharListaPets(request, response, lista);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("usuarioRole");
        Long idLogado = (Long) session.getAttribute("usuarioId");
        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Pet petParaDeletar = petDao.buscarPorId(id);
            if (petParaDeletar == null || petParaDeletar.getTutor() == null) {
                response.sendRedirect(request.getContextPath() + "/pets");
                return;
            }
            if ("TUTOR".equals(role) && !idLogado.equals(petParaDeletar.getTutor().getId())) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }
            petDao.deletar(id);
            response.sendRedirect(request.getContextPath() + "/pets");
            return;
        }

        String nome = request.getParameter("nomePet");
        String raca = request.getParameter("racaPet");
        LocalDate dataNascimento = LocalDate.parse(request.getParameter("dataNascimentoPet"));

        Long tutorId;
        if ("TUTOR".equals(role)) {
            tutorId = idLogado;
        } else {
            tutorId = Long.parseLong(request.getParameter("tutorId"));
        }

        Tutor tutorPet = new Tutor();
        tutorPet.setId(tutorId);

        if ("atualizar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Pet petAtual = petDao.buscarPorId(id);

            if (petAtual == null || petAtual.getTutor() == null) {
                response.sendRedirect(request.getContextPath() + "/pets");
                return;
            }

            if ("TUTOR".equals(role) && !idLogado.equals(petAtual.getTutor().getId())) {
                response.sendRedirect(request.getContextPath() + "/acesso-negado");
                return;
            }

            Pet pet = new Pet();
            pet.setId(id);
            pet.setNome(nome);
            pet.setRaca(raca);
            pet.setDataNascimento(dataNascimento);
            pet.setTutor(tutorPet);

            petDao.atualizar(pet);

        } else {
            Pet novoPet = new Pet();
            novoPet.setNome(nome);
            novoPet.setRaca(raca);
            novoPet.setDataNascimento(dataNascimento);
            novoPet.setTutor(tutorPet);

            petDao.salvar(novoPet);
        }

        response.sendRedirect(request.getContextPath() + "/pets");
    }
}