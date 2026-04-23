package com.uff.gestaoclinicaveterinaria.model;

public class Veterinario {
    private Long id;
    private String nome;
    private String crmv;
    private String especialidade;

    public Veterinario(){}

    public Veterinario(String nome, String crmv, String especialidade){
        this.nome = nome;
        this.crmv = crmv;
        this.especialidade = especialidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}
