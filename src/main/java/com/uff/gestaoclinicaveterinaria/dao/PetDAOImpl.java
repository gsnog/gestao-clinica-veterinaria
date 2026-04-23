package com.uff.gestaoclinicaveterinaria.dao;

import com.uff.gestaoclinicaveterinaria.model.Pet;
import com.uff.gestaoclinicaveterinaria.model.Tutor;
import com.uff.gestaoclinicaveterinaria.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAOImpl implements PetDAO {

    @Override
    public void salvar(Pet pet) {
        String sql = "INSERT INTO pet (nome, raca, data_nascimento, tutor_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getRaca());
            stmt.setDate(3, Date.valueOf(pet.getDataNascimento())); // LocalDate para SQL Date
            stmt.setLong(4, pet.getTutor().getId()); // Extrai o ID do manequim
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Pet> listarTodos() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getLong("id"));
                pet.setNome(rs.getString("nome"));
                pet.setRaca(rs.getString("raca"));
                if (rs.getDate("data_nascimento") != null) {
                    pet.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                }

                Tutor tutor = new Tutor();
                tutor.setId(rs.getLong("tutor_id"));
                pet.setTutor(tutor);

                pets.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    @Override
    public Pet buscarPorId(Long id) {
        String sql = "SELECT * FROM pet WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getLong("id"));
                    pet.setNome(rs.getString("nome"));
                    pet.setRaca(rs.getString("raca"));
                    if (rs.getDate("data_nascimento") != null) {
                        pet.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    }

                    Tutor tutor = new Tutor();
                    tutor.setId(rs.getLong("tutor_id"));
                    pet.setTutor(tutor);

                    return pet;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void atualizar(Pet pet) {
        String sql = "UPDATE pet SET nome = ?, raca = ?, data_nascimento = ?, tutor_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getRaca());
            stmt.setDate(3, Date.valueOf(pet.getDataNascimento()));
            stmt.setLong(4, pet.getTutor().getId());
            stmt.setLong(5, pet.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM pet WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}