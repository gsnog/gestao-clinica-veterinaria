package com.uff.gestaoclinicaveterinaria.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.uff.gestaoclinicaveterinaria.model.Consulta;
import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Veterinario;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;
import com.uff.gestaoclinicaveterinaria.util.SearchFilterUtil;

public class ConsultaDAOImpl implements ConsultaDAO {

    private static final String SQL_SELECT_JOIN = "SELECT * FROM v_detalhes_consulta ";
    private static final String SQL_NORMALIZE_ACCENTS =
            "translate(lower(%s), 'áàãâäéèêëíìîïóòõôöúùûüç', 'aaaaaeeeeiiiiooooouuuuc')";

    @Override
    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consulta (data_consulta, motivo, diagnostico, pet_id, veterinario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataConsulta()));
            stmt.setString(2, consulta.getMotivo());
            stmt.setString(3, consulta.getDiagnostico());
            stmt.setLong(4, consulta.getPet().getId());
            stmt.setLong(5, consulta.getVeterinario().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Consulta consulta) {
        String sql = "UPDATE consulta SET data_consulta = ?, motivo = ?, diagnostico = ?, pet_id = ?, veterinario_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataConsulta()));
            stmt.setString(2, consulta.getMotivo());
            stmt.setString(3, consulta.getDiagnostico());
            stmt.setLong(4, consulta.getPet().getId());
            stmt.setLong(5, consulta.getVeterinario().getId());
            stmt.setLong(6, consulta.getId());
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
        String sql = SQL_SELECT_JOIN + "WHERE consulta_id = ?";
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
        String sql = SQL_SELECT_JOIN + "WHERE pet_id = ?";
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
        String sql = SQL_SELECT_JOIN + "WHERE vet_id = ? AND DATE(data_consulta) = ?";
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

    public List<Consulta> buscarPorTutor(Long tutorId) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = SQL_SELECT_JOIN + "WHERE pet_id IN (SELECT id FROM pet WHERE tutor_id = ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, tutorId);
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
    public List<Consulta> filtrar(String busca, LocalDate data) {
        List<Consulta> lista = new ArrayList<>();
        String sql = SQL_SELECT_JOIN + "WHERE 1=1";
        String buscaNormalizada = SearchFilterUtil.normalize(busca);

        if (!buscaNormalizada.isEmpty()) {
            sql += " AND ("
                    + String.format(SQL_NORMALIZE_ACCENTS, "pet_nome") + " LIKE ? OR "
                    + String.format(SQL_NORMALIZE_ACCENTS, "vet_nome") + " LIKE ?"
                    + ")";
        }

        if (data != null) {
            sql += " AND DATE(data_consulta) = ?";
        }

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;

            if (!buscaNormalizada.isEmpty()) {
                stmt.setString(index++, buscaNormalizada + "%");
                stmt.setString(index++, buscaNormalizada + "%");
            }

            if (data != null) {
                stmt.setDate(index++, Date.valueOf(data));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairConsultaComJoin(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private Consulta extrairConsultaComJoin(ResultSet rs) throws SQLException {
        Consulta consulta = new Consulta();
        consulta.setId(rs.getLong("consulta_id"));
        if (rs.getTimestamp("data_consulta") != null) {
            consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
        }
        consulta.setMotivo(rs.getString("motivo"));
        consulta.setDiagnostico(rs.getString("diagnostico"));

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
