package com.uff.gestaoclinicaveterinaria.dto;

import java.time.LocalDate;

public record PetRequestDTO(Long id, String nome, String raca, LocalDate dataNascimento, Long tutorId) {
}
