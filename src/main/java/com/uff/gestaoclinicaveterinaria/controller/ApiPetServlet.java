package com.uff.gestaoclinicaveterinaria.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uff.gestaoclinicaveterinaria.dao.PetDAO;
import com.uff.gestaoclinicaveterinaria.dao.PetDAOImpl;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAO;
import com.uff.gestaoclinicaveterinaria.dao.TutorDAOImpl;
import com.uff.gestaoclinicaveterinaria.dto.PetRequestDTO;
import com.uff.gestaoclinicaveterinaria.dto.PetResponseDTO;
import com.uff.gestaoclinicaveterinaria.dto.RefDTO;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.InputSanitizer;
import com.uff.gestaoclinicaveterinaria.util.InputValidator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/api/pets", "/api/pets/*"})
public class ApiPetServlet extends ApiServlet {

    private final PetDAO petDAO = new PetDAOImpl();
    private final TutorDAO tutorDAO = new TutorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isBlank()) {
            Long idPet = extrairIdDoPath(request);
            if (idPet == null) {
                responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador inválido.");
                return;
            }

            Pet pet = petDAO.buscarPorId(idPet);
            if (pet == null || pet.getTutor() == null) {
                responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Pet não encontrado.");
                return;
            }

            if ("TUTOR".equals(role) && !idLogado.equals(pet.getTutor().getId())) {
                responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode visualizar pets vinculados ao seu cadastro.");
                return;
            }

            responderJson(response, HttpServletResponse.SC_OK, paraDTO(pet));
            return;
        }

        List<Pet> lista = "TUTOR".equals(role) ? petDAO.buscarPorTutor(idLogado) : petDAO.listarTodos();
        responderJson(response, HttpServletResponse.SC_OK, lista.stream().map(this::paraDTO).toList());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        PetRequestDTO corpo;
        try {
            corpo = lerCorpo(request, PetRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        Map<String, String> erros = validarPet(corpo);
        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados do pet inválidos.", erros);
            return;
        }

        Tutor tutor = resolverTutorParaEscrita(role, idLogado, corpo.tutorId());
        if (tutor == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Tutor inválido.", Map.of("tutorId", "Tutor inválido."));
            return;
        }

        Pet novoPet = new Pet();
        novoPet.setNome(InputSanitizer.sanitizarTexto(corpo.nome()));
        novoPet.setRaca(InputSanitizer.sanitizarTexto(corpo.raca()));
        novoPet.setDataNascimento(corpo.dataNascimento());
        novoPet.setTutor(tutor);

        petDAO.salvar(novoPet);

        responderJson(response, HttpServletResponse.SC_CREATED, paraDTO(novoPet));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        PetRequestDTO corpo;
        try {
            corpo = lerCorpo(request, PetRequestDTO.class);
        } catch (Exception e) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Corpo da requisição inválido.");
            return;
        }

        if (corpo == null || corpo.id() == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador do pet é obrigatório.",
                    Map.of("id", "Identificador do pet é obrigatório."));
            return;
        }

        Pet petAtual = petDAO.buscarPorId(corpo.id());
        if (petAtual == null || petAtual.getTutor() == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Pet não encontrado.");
            return;
        }

        if ("TUTOR".equals(role) && !idLogado.equals(petAtual.getTutor().getId())) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode editar pets vinculados ao seu cadastro.");
            return;
        }

        Map<String, String> erros = validarPet(corpo);
        if (!erros.isEmpty()) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Dados do pet inválidos.", erros);
            return;
        }

        Tutor tutor = resolverTutorParaEscrita(role, idLogado, corpo.tutorId());
        if (tutor == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Tutor inválido.", Map.of("tutorId", "Tutor inválido."));
            return;
        }

        Pet petAtualizado = new Pet();
        petAtualizado.setId(corpo.id());
        petAtualizado.setNome(InputSanitizer.sanitizarTexto(corpo.nome()));
        petAtualizado.setRaca(InputSanitizer.sanitizarTexto(corpo.raca()));
        petAtualizado.setDataNascimento(corpo.dataNascimento());
        petAtualizado.setTutor(tutor);

        petDAO.atualizar(petAtualizado);

        responderJson(response, HttpServletResponse.SC_OK, paraDTO(petAtualizado));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String role = roleUsuarioLogado(request);
        Long idLogado = idUsuarioLogado(request);

        Long id = extrairIdDoPath(request);
        if (id == null) {
            responderErro(response, HttpServletResponse.SC_BAD_REQUEST, "Identificador inválido.");
            return;
        }

        Pet pet = petDAO.buscarPorId(id);
        if (pet == null || pet.getTutor() == null) {
            responderErro(response, HttpServletResponse.SC_NOT_FOUND, "Pet não encontrado.");
            return;
        }

        if ("TUTOR".equals(role) && !idLogado.equals(pet.getTutor().getId())) {
            responderErro(response, HttpServletResponse.SC_FORBIDDEN, "Você só pode excluir pets vinculados ao seu cadastro.");
            return;
        }

        petDAO.deletar(id);
        responderJson(response, HttpServletResponse.SC_OK, corpoJson("success", true));
    }

    private Map<String, String> validarPet(PetRequestDTO corpo) {
        Map<String, String> erros = new LinkedHashMap<>();
        if (corpo == null || InputValidator.isNullOrBlank(corpo.nome())) {
            erros.put("nome", "Nome é obrigatório.");
        }
        if (corpo == null || InputValidator.isNullOrBlank(corpo.raca())) {
            erros.put("raca", "Raça é obrigatória.");
        }
        if (corpo == null || corpo.dataNascimento() == null || corpo.dataNascimento().isAfter(LocalDate.now())) {
            erros.put("dataNascimento", "Data de nascimento inválida.");
        }
        return erros;
    }

    /**
     * Tutor só pode escrever para si mesmo (o tutorId enviado é ignorado);
     * veterinário precisa indicar um tutor existente.
     */
    private Tutor resolverTutorParaEscrita(String role, Long idLogado, Long tutorIdInformado) {
        if ("TUTOR".equals(role)) {
            return tutorDAO.buscarPorId(idLogado);
        }
        return tutorIdInformado != null ? tutorDAO.buscarPorId(tutorIdInformado) : null;
    }

    private PetResponseDTO paraDTO(Pet pet) {
        Tutor tutor = pet.getTutor();
        RefDTO tutorRef = (tutor != null && tutor.getId() != null) ? new RefDTO(tutor.getId(), tutor.getNome()) : null;
        return new PetResponseDTO(pet.getId(), pet.getNome(), pet.getRaca(), pet.getDataNascimento(), tutorRef);
    }
}
