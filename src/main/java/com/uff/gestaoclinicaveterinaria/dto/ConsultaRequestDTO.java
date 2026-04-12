package com.uff.gestaoclinicaveterinaria.dto;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(LocalDateTime dataConsulta, String motivo, Long petId, Long veterinarioId) {
}
