package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Consulta;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAOImpl implements ConsultaDAO {

    private static final String SQL_SELECT_JOIN =
            "SELECT c.id AS consulta_id, c.data_consulta, c.motivo, " +
                    "p.id AS pet_id, p.nome AS pet_nome, p.raca AS pet_raca, " +
                    "v.id AS vet_id, v.nome AS vet_nome, v.especialidade AS vet_especialidade " +
                    "FROM consulta c " +
                    "INNER JOIN pet p ON c.pet_id = p.id " +
                    "INNER JOIN veterinario v ON c.veterinario_id = v.id ";

    @Override
    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consulta (data_consulta, motivo, pet_id, veterinario_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataConsulta()));
            stmt.setString(2, consulta.getMotivo());
            stmt.setLong(3, consulta.getPet().getId());
            stmt.setLong(4, consulta.getVeterinario().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Consulta consulta) {
        String sql = "UPDATE consulta SET data_consulta = ?, motivo = ?, pet_id = ?, veterinario_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataConsulta()));
            stmt.setString(2, consulta.getMotivo());
            stmt.setLong(3, consulta.getPet().getId());
            stmt.setLong(4, consulta.getVeterinario().getId());
            stmt.setLong(5, consulta.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM consulta WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Consulta> listarTodos() {
        List<Consulta> consultas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_JOIN);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                consultas.add(extrairConsultaComJoin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    @Override
    public Consulta buscarPorId(Long id) {
        String sql = SQL_SELECT_JOIN + "WHERE c.id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairConsultaComJoin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Consulta> buscarPorPet(Long petId) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = SQL_SELECT_JOIN + "WHERE c.pet_id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, petId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(extrairConsultaComJoin(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    @Override
    public List<Consulta> buscarPorData(Long veterinarioId, LocalDate data) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = SQL_SELECT_JOIN + "WHERE c.veterinario_id = ? AND DATE(c.data_consulta) = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, veterinarioId);
            stmt.setDate(2, Date.valueOf(data));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(extrairConsultaComJoin(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    private Consulta extrairConsultaComJoin(ResultSet rs) throws SQLException {
        Consulta consulta = new Consulta();
        consulta.setId(rs.getLong("consulta_id"));
        if (rs.getTimestamp("data_consulta") != null) {
            consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
        }
        consulta.setMotivo(rs.getString("motivo"));

        Pet pet = new Pet();
        pet.setId(rs.getLong("pet_id"));
        pet.setNome(rs.getString("pet_nome"));
        pet.setRaca(rs.getString("pet_raca"));
        consulta.setPet(pet);

        Veterinario vet = new Veterinario();
        vet.setId(rs.getLong("vet_id"));
        vet.setNome(rs.getString("vet_nome"));
        vet.setEspecialidade(rs.getString("vet_especialidade"));
        consulta.setVeterinario(vet);

        return consulta;
    }
}