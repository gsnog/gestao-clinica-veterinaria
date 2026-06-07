package com.uff.gestaoclinicaveterinaria.dto;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(Long id, LocalDateTime dataConsulta, String motivo, String diagnostico, RefDTO pet, RefDTO veterinario) {
}
