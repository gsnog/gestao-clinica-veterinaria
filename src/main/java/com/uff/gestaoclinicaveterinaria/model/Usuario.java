package com.uff.gestaoclinicaveterinaria.model;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senhaHash;
    private String salt;
    private String role;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senhaHash, String salt, String role) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.salt = salt;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}