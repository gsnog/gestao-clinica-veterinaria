package com.uff.gestaoclinicaveterinaria.model;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private LocalDateTime dataConsulta;
    private String motivo;
    private Pet pet;
    private Veterinario veterinario;

    public Consulta(){}

    public Consulta(LocalDateTime dataConsulta, String motivo, Pet pet, Veterinario veterinario){
        this.dataConsulta = dataConsulta;
        this.motivo = motivo;
        this.pet = pet;
        this.veterinario = veterinario;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
}
