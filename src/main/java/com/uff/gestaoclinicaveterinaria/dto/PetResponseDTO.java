package com.uff.gestaoclinicaveterinaria.dto;

import java.time.LocalDate;

public record PetResponseDTO(Long id, String nome, String raca, LocalDate dataNascimento, String tutorNome, String tutorTelefone) {
}
