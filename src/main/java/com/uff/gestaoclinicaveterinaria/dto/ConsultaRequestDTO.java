package com.uff.gestaoclinicaveterinaria.dto;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(Long id, Long petId, Long veterinarioId, LocalDateTime dataConsulta, String motivo, String diagnostico) {
}
