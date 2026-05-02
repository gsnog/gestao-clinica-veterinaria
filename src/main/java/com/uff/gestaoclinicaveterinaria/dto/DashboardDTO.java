package com.uff.gestaoclinicaveterinaria.model;

public class DashboardDTO {

    private long totalTutores;
    private long totalPets;
    private long totalVeterinarios;
    private long totalConsultas;

    public long getTotalTutores() {
        return totalTutores;
    }

    public void setTotalTutores(long totalTutores) {
        this.totalTutores = totalTutores;
    }

    public long getTotalPets() {
        return totalPets;
    }

    public void setTotalPets(long totalPets) {
        this.totalPets = totalPets;
    }

    public long getTotalVeterinarios() {
        return totalVeterinarios;
    }

    public void setTotalVeterinarios(long totalVeterinarios) {
        this.totalVeterinarios = totalVeterinarios;
    }

    public long getTotalConsultas() {
        return totalConsultas;
    }

    public void setTotalConsultas(long totalConsultas) {
        this.totalConsultas = totalConsultas;
    }
}