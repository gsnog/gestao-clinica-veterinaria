package com.uff.gestaoclinicaveterinaria.model;

import java.time.LocalDate;

public class Pet {
    private Long id;
    private String nome;
    private String raca;
    private LocalDate dataNascimento;
    private Tutor tutor;

    public Pet(){}

    public Pet(String nome, String raca, LocalDate dataNascimento, Tutor tutor){
        this.nome = nome;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.tutor = tutor;
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

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}
