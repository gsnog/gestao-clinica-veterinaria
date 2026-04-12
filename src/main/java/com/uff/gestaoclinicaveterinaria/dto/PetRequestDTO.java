package com.uff.gestaoclinicaveterinaria.dto;

import java.time.LocalDate;

public record PetRequestDTO(String nome, String raca, LocalDate dataNascimento, Long tutorId) {

}
